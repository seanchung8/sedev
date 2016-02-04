
Template.clustersList.allclusters = function () {
      var all =  Clusters.find();
      all.forEach(function (item) {
         console.log("Cluster is: " +  item.name);
       });
       return all;
}

Template.clustersList.helpers({
  clusters: function() {
      return Clusters.find();
//    return Clusters.find({}, {sort: {submitted: -1}});
  }
});
