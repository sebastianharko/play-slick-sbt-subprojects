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
