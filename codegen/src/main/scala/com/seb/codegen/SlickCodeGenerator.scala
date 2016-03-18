package com.seb.codegen

object Config{
  // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  val url = "jdbc:postgresql://test.cx5oybgdkpqs.us-west-2.rds.amazonaws.com:5432/test?user=root&password=twistandshout"
  val jdbcDriver = "org.postgresql.Driver"
  val slickProfile = slick.driver.PostgresDriver
}

import slick.driver.PostgresDriver
import Config._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  *  This customizes the Slick code generator. We only do simple name mappings.
  *  For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
  */
object CustomizedCodeGenerator{
  def main(args: Array[String]) = {

    Await.ready(
      codegen.map(_.writeToFile(
        "slick.driver.PostgresDriver",
        args(0),
        "com.seb.db",
        "Tables",
        "Tables.scala"
      )),
      20 seconds
    )
  }

  val db = PostgresDriver.api.Database.forURL(url,driver=jdbcDriver)
  // filter out desired tables
  val included = Seq("COFFEES","SUPPLIERS","COF_INVENTORY")
  val codegen = db.run{
    PostgresDriver.defaultTables.map(_.filterNot(t => included contains t.name.name)).flatMap( PostgresDriver.createModelBuilder(_,false).buildModel )
  }.map{ model =>
    new slick.codegen.SourceCodeGenerator(model){
      // customize Scala entity name (case class, etc.)
      override def entityName = dbTableName => dbTableName match {
        case "COFFEES" => "Coffee"
        case "SUPPLIERS" => "Supplier"
        case "COF_INVENTORY" => "CoffeeInventoryItem"
        case _ => super.entityName(dbTableName)
      }
      // customize Scala table name (table class, table values, ...)
      override def tableName = dbTableName => dbTableName match {
        case "COF_INVENTORY" => "CoffeeInventory"
        case _ => super.tableName(dbTableName)
      }
      // override generator responsible for tables
      override def Table = new Table(_){
        table =>
        // customize table value (TableQuery) name (uses tableName as a basis)
        override def TableValue = new TableValue{
          override def rawName = super.rawName.uncapitalize
        }
        // override generator responsible for columns
        override def Column = new Column(_){
          // customize Scala column names
          override def rawName = (table.model.name.table,this.model.name) match {
            case ("COFFEES","COF_NAME") => "name"
            case ("COFFEES","SUP_ID") => "supplierId"
            case ("SUPPLIERS","SUP_ID") => "id"
            case ("SUPPLIERS","SUP_NAME") => "name"
            case ("COF_INVENTORY","QUAN") => "quantity"
            case ("COF_INVENTORY","COF_NAME") => "coffeeName"
            case _ => super.rawName
          }
        }
      }
    }
  }
}