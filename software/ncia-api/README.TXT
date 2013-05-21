This contains the domain model for the NCIACoreService data service.  We kept
the ncia-api to reflect its affinity with NCIACoreService.

This project used to contain ncia-api.wars for Oracle and MySql.  Up to and
including 5.0.x, the caGrid data service would talk to the caCORE SDK data
service through HTTP.  As of the mythical 5.1, this was switched to use
local api.