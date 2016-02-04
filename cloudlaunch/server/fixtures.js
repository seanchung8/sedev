
if (Clusters.find().count() === 0) {
  Clusters.insert({
    provider: 'Hortoncloud',
    name: 'My first Cluster',
    blueprint: 'small',
    username: 'test',
    password: 'test',
    sshkey: 'ABCDEFG',
    owner: 'nobody'
  });
}

