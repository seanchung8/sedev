Template.clusterSubmit.events({
  'submit form': function(e) {
    e.preventDefault();

    var cluster = {
      provider: $(e.target).find('[name=provider]').val(),
      blueprint: $(e.target).find('[name=blueprint]').val(),
      name: $(e.target).find('[name=name]').val(),
      username: $(e.target).find('[name=username]').val(),
      password: $(e.target).find('[name=password]').val(),
      sshkey: $(e.target).find('[name=sshkey]').val()
    }
    Meteor.call('launch', cluster, function(error, id) {
      if (error)
        return alert(error.reason);

      Router.go('clusterPage', {_id: id});
    });
  }

,

   'change #provider': function(event) {
     Session.set("selected_provider", event.currentTarget.value);
  }


});
