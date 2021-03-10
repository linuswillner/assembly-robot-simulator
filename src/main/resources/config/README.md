This directory contains configuration files used in the operation of the application.

### File reference

#### config.json

Global application configuration. Contains default
configuration for all simulation settings, as well as some
mission-critical non-user-alterable settings for the application.

**Note:** Any edits to this file **must** be accompanied by edits to the
`shared.config.model` package in order for the configuration changes to
be usable in the application.

#### hibernate.cfg.xml

Partial configuration for the
[Hibernate ORM](https://hibernate.org/orm/). Any static options that do
not need to be changed at runtime are changed here.

**Note:** Database addresses, authentication information or any other
data that is of sensitive nature isn't configured in this file. This
configuration should, and will, take place through environment
variables. See the project-level README for more information.
