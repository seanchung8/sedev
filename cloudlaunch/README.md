Cloudlaunch
===========

## Overview
Cloudlaunch is a web application designed to easily launch HDP clusters in the cloud. You will need an account with a supported cloud provider to use this service.

## Installation

Cloudlaunch depends on [Meteor](https://www.meteor.com/) and [Iron Router](http://atmospherejs.com/package/iron-router).
```
curl https://install.meteor.com/ | sh
# The following is required until Meteor supports Atmosphere natively
yum install npm -y
npm install -g meteorite
```

Get Cloudlaunch and add the iron-router module:
```
git clone git@github.com:hortonworks/sedev.git
cd sedev/cloundlaunch
mrt add iron-router 
```

## Run

```
cd cloudlaunch
meteor
```

Navigate to http://server_name:3000/.
