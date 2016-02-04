Clusters = new Meteor.Collection('clusters');
Clusters.allow({
  update: ownsDocument,
  remove: ownsDocument
});
Clusters.deny({
  update: function(userId, launch, fieldNames) {
    // may only edit the following two fields:
    return (_.without(fieldNames, 'name', 'username').length > 0);
  }
});
Meteor.methods({
  launch: function(clusterAttributes) {
    var user = Meteor.user(),
      clusterWithSameName = Clusters.findOne({name: clusterAttributes.name});

    // ensure the user is logged in
    if (!user)
      throw new Meteor.Error(401, "You need to login to launch a cluster");

    // ensure the cluster has a name
    if (!clusterAttributes.name)
      throw new Meteor.Error(422, 'Please fill in a name');

    // check that there are no previous posts with the same link
    /*
    if (postAttributes.url && postWithSameLink) {
      throw new Meteor.Error(302, 
        'This link has already been posted', 
        postWithSameLink._id);
    }
    */

    // pick out the whitelisted keys
    var cluster = _.extend(_.pick(clusterAttributes, 'provider', 'blueprint', 'name', 'username', 
	'password', 'sshkey'), {
      userId: user._id, 
      owner: user.username, 
      submitted: new Date().getTime()
    });

    var clusterId = Clusters.insert(cluster);

    // queue launch request
    // TODO

    return clusterId;
  }
});
