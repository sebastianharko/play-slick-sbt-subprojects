Compile All
===========

`sbt compile`

Select individual project and compile
=====================================
```
sbt
> project frontend
> compile
```
Run code generator and dump result in db/src/main/scala/com/seb/db/Tables.scala
===============================================================================

```
sbt
> project db
> genTables
> compile
```

Swagger
=======
```
http://localhost:9000/docs/swagger-ui/index.html?url=/docs/swagger.json
```

