package com.seb.db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = coffeeInventory.schema ++ coffees.schema ++ suppliers.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table coffeeInventory
   *  @param warehouseId Database column WAREHOUSE_ID SqlType(INTEGER)
   *  @param coffeeName Database column COF_NAME SqlType(VARCHAR), Length(32,true)
   *  @param supId Database column SUP_ID SqlType(INTEGER)
   *  @param quantity Database column QUAN SqlType(INTEGER)
   *  @param dateVal Database column DATE_VAL SqlType(TIMESTAMP) */
  case class CoffeeInventoryItem(warehouseId: Int, coffeeName: String, supId: Int, quantity: Int, dateVal: Option[java.sql.Timestamp])
  /** GetResult implicit for fetching CoffeeInventoryItem objects using plain SQL queries */
  implicit def GetResultCoffeeInventoryItem(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]]): GR[CoffeeInventoryItem] = GR{
    prs => import prs._
    CoffeeInventoryItem.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<?[java.sql.Timestamp]))
  }
  /** Table description of table COF_INVENTORY. Objects of this class serve as prototypes for rows in queries. */
  class CoffeeInventory(_tableTag: Tag) extends Table[CoffeeInventoryItem](_tableTag, "COF_INVENTORY") {
    def * = (warehouseId, coffeeName, supId, quantity, dateVal) <> (CoffeeInventoryItem.tupled, CoffeeInventoryItem.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(warehouseId), Rep.Some(coffeeName), Rep.Some(supId), Rep.Some(quantity), dateVal).shaped.<>({r=>import r._; _1.map(_=> CoffeeInventoryItem.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column WAREHOUSE_ID SqlType(INTEGER) */
    val warehouseId: Rep[Int] = column[Int]("WAREHOUSE_ID")
    /** Database column COF_NAME SqlType(VARCHAR), Length(32,true) */
    val coffeeName: Rep[String] = column[String]("COF_NAME", O.Length(32,varying=true))
    /** Database column SUP_ID SqlType(INTEGER) */
    val supId: Rep[Int] = column[Int]("SUP_ID")
    /** Database column QUAN SqlType(INTEGER) */
    val quantity: Rep[Int] = column[Int]("QUAN")
    /** Database column DATE_VAL SqlType(TIMESTAMP) */
    val dateVal: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("DATE_VAL")

    /** Foreign key referencing coffees (database name CONSTRAINT_5) */
    lazy val coffeesFk = foreignKey("CONSTRAINT_5", coffeeName, coffees)(r => r.name, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing suppliers (database name CONSTRAINT_57) */
    lazy val suppliersFk = foreignKey("CONSTRAINT_57", supId, suppliers)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table coffeeInventory */
  lazy val coffeeInventory = new TableQuery(tag => new CoffeeInventory(tag))

  /** Entity class storing rows of table coffees
   *  @param name Database column COF_NAME SqlType(VARCHAR), PrimaryKey, Length(32,true)
   *  @param supplierId Database column SUP_ID SqlType(INTEGER)
   *  @param price Database column PRICE SqlType(DECIMAL)
   *  @param sales Database column SALES SqlType(INTEGER)
   *  @param total Database column TOTAL SqlType(INTEGER) */
  case class Coffee(name: String, supplierId: Int, price: scala.math.BigDecimal, sales: Int, total: Int)
  /** GetResult implicit for fetching Coffee objects using plain SQL queries */
  implicit def GetResultCoffee(implicit e0: GR[String], e1: GR[Int], e2: GR[scala.math.BigDecimal]): GR[Coffee] = GR{
    prs => import prs._
    Coffee.tupled((<<[String], <<[Int], <<[scala.math.BigDecimal], <<[Int], <<[Int]))
  }
  /** Table description of table COFFEES. Objects of this class serve as prototypes for rows in queries. */
  class Coffees(_tableTag: Tag) extends Table[Coffee](_tableTag, "COFFEES") {
    def * = (name, supplierId, price, sales, total) <> (Coffee.tupled, Coffee.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(name), Rep.Some(supplierId), Rep.Some(price), Rep.Some(sales), Rep.Some(total)).shaped.<>({r=>import r._; _1.map(_=> Coffee.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column COF_NAME SqlType(VARCHAR), PrimaryKey, Length(32,true) */
    val name: Rep[String] = column[String]("COF_NAME", O.PrimaryKey, O.Length(32,varying=true))
    /** Database column SUP_ID SqlType(INTEGER) */
    val supplierId: Rep[Int] = column[Int]("SUP_ID")
    /** Database column PRICE SqlType(DECIMAL) */
    val price: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("PRICE")
    /** Database column SALES SqlType(INTEGER) */
    val sales: Rep[Int] = column[Int]("SALES")
    /** Database column TOTAL SqlType(INTEGER) */
    val total: Rep[Int] = column[Int]("TOTAL")

    /** Foreign key referencing suppliers (database name CONSTRAINT_63) */
    lazy val suppliersFk = foreignKey("CONSTRAINT_63", supplierId, suppliers)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table coffees */
  lazy val coffees = new TableQuery(tag => new Coffees(tag))

  /** Entity class storing rows of table suppliers
   *  @param id Database column SUP_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param name Database column SUP_NAME SqlType(VARCHAR), Length(40,true)
   *  @param street Database column STREET SqlType(VARCHAR), Length(40,true)
   *  @param city Database column CITY SqlType(VARCHAR), Length(20,true)
   *  @param state Database column STATE SqlType(CHAR), Length(2,false)
   *  @param zip Database column ZIP SqlType(CHAR), Length(5,false) */
  case class Supplier(id: Int, name: String, street: String, city: String, state: String, zip: Option[String])
  /** GetResult implicit for fetching Supplier objects using plain SQL queries */
  implicit def GetResultSupplier(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[Supplier] = GR{
    prs => import prs._
    Supplier.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table SUPPLIERS. Objects of this class serve as prototypes for rows in queries. */
  class Suppliers(_tableTag: Tag) extends Table[Supplier](_tableTag, "SUPPLIERS") {
    def * = (id, name, street, city, state, zip) <> (Supplier.tupled, Supplier.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(street), Rep.Some(city), Rep.Some(state), zip).shaped.<>({r=>import r._; _1.map(_=> Supplier.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column SUP_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("SUP_ID", O.AutoInc, O.PrimaryKey)
    /** Database column SUP_NAME SqlType(VARCHAR), Length(40,true) */
    val name: Rep[String] = column[String]("SUP_NAME", O.Length(40,varying=true))
    /** Database column STREET SqlType(VARCHAR), Length(40,true) */
    val street: Rep[String] = column[String]("STREET", O.Length(40,varying=true))
    /** Database column CITY SqlType(VARCHAR), Length(20,true) */
    val city: Rep[String] = column[String]("CITY", O.Length(20,varying=true))
    /** Database column STATE SqlType(CHAR), Length(2,false) */
    val state: Rep[String] = column[String]("STATE", O.Length(2,varying=false))
    /** Database column ZIP SqlType(CHAR), Length(5,false) */
    val zip: Rep[Option[String]] = column[Option[String]]("ZIP", O.Length(5,varying=false))
  }
  /** Collection-like TableQuery object for table suppliers */
  lazy val suppliers = new TableQuery(tag => new Suppliers(tag))
}
