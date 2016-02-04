
Meteor.publish('clusters', function() {
  return Clusters.find();
});
