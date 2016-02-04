Router.configure({
  layoutTemplate: 'layout',
  loadingTemplate: 'loading',
  waitOn: function() { 
    return Meteor.subscribe('clusters'); }
});

Router.map(function() {
  this.route('clusterSubmit', {path: '/'});
  this.route('clusterPage', {
    path: '/clusters/:_id',
    data: function() { return Clusters.findOne(this.params._id); }
  });
   this.route('clusterEdit', {
    path: '/clusters/:_id/edit',
    data: function() { return Clusters.findOne(this.params._id); }
   });
  this.route('clusterSubmit', {path: '/launch'});
  this.route('clustersList', {path: '/clusters'});
});

var requireLogin = function(pause) {
  if (! Meteor.user()) {
    if (Meteor.loggingIn())
      this.render(this.loadingTemplate);
    else
      this.render('accessDenied');
    pause();
  }
}
Router.onBeforeAction('loading');
Router.onBeforeAction(requireLogin, {only: 'clusterSubmit'});
