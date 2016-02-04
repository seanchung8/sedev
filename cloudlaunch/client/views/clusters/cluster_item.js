Template.clusterItem.helpers({
  ownCluster: function() {
    return this.userId == Meteor.userId();
  },
  domain: function() {
    return this.name;
  }
});
