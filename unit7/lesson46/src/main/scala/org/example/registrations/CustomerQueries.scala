package org.example.registrations

import io.getquill.{PostgresAsyncContext, SnakeCase}
import scala.concurrent.{ExecutionContext, Future}

case class Customer(id: Int, name: String)

class CustomerQueries(ctx: PostgresAsyncContext[SnakeCase.type])
                     (implicit ec: ExecutionContext) {
  import ctx._

  private val customers = quote { query[Customer] }

  def all: Future[List[Customer]] = {
    // Generated SQL: SELECT x.id, x.name FROM customer x
    run(customers)
  }

  def nameById(id: Int): Future[List[String]] = {
    // Generated SQL: SELECT x1.name FROM customer x1 WHERE x1.id = ?
    val q = quote {
      customers.filter(_.id == lift(id))
              .map(_.name)
    }
    run(q)
  }

  def save(customer: Customer): Future[String] = {
    // Generated SQL: INSERT INTO customer (id,name) VALUES (?, ?) RETURNING name
    val q = quote {
      customers.insert(lift(customer))
              .returning(_.name)
    }
    run(q)
  }

  def updateNameById(id: Int, nameToUpdate: String): Future[Long] = {
    // Generated SQL: UPDATE customer SET name = ? WHERE id = ?
    val q = quote {
      customers.filter(_.id == lift(id))
              .update(_.name -> lift(nameToUpdate))
    }
    run(q)
  }

  def deleteById(id: Int): Future[Long] = {
    // Generated SQL: DELETE FROM customer WHERE id = ?
    val q = quote {
      customers.filter(_.id == lift(id)).delete
    }
    run(q)
  }

}
