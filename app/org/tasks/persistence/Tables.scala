package org.tasks.persistence

import java.sql.Date

import slick.driver.H2Driver.api._

/**
  * Created by jayonhuh on 1/30/17.
  * The tables file
  */

// Case class for task that has the same fields as the table.
case class Task(id: Int = 0, title: String, note: String, categoryID: Option[Int] = None, creationDate: Date = new Date(System.currentTimeMillis()),
                dueDate: Option[Date] = None, isDone: Boolean = false)

// A task Table with 5 columns: id, title, note, topicID, and dueDate
class Tasks(tag: Tag)
  extends Table[Task](tag, "TASKS") {

  // This is the primary key column: ID, this is automatically generated incrementally.
  def id: Rep[Int] = column[Int]("TASK_ID", O.PrimaryKey, O.AutoInc)

  // column for the title of the task
  def title: Rep[String] = column[String]("TITLE")

  // column for the notes written about the task
  def note: Rep[String] = column[String]("NOTE")

  // column to keep track of what topic the task is in
  def categoryID: Rep[Option[Int]] = column[Option[Int]]("CATEGORY_ID", O.Default(None))

  // column for the creation date of the task.
  def creationDate: Rep[Date] = column[Date]("CREATION_DATE", O.Default(new Date(System.currentTimeMillis())))

  // column for the due date
  def dueDate: Rep[Option[Date]] = column[Option[Date]]("DUE_DATE")

  // column for the boolean representing whether the task has been completed or not, defaulted as false
  def isDone: Rep[Boolean] = column[Boolean]("IS_DONE", O.Default(false))

  // composite key of title, note, and dueDate
  def compositeKey = index("TASK_COMPOSITE_KEY", (title, note, dueDate), unique = true)

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, title, note, categoryID, creationDate, dueDate, isDone) <> (Task.tupled, Task.unapply)

}

// Case class for Task Topics, with the same fields as the table.
case class Category(id: Int = 0, description: String)

// TaskTopic table with 2 columns: id, description, and taskID
class Categories(tag: Tag)
  extends Table[Category](tag, "CATEGORY") {

  // column for ID, which will be the primary key and will be automatically generated incrementally
  def id: Rep[Int] = column[Int]("CATEGORY_ID", O.PrimaryKey, O.AutoInc)

  // column for description
  def description: Rep[String] = column[String]("DESCRIPTION")

  // * projection
  def * = (id, description) <> (Category.tupled, Category.unapply)

}

// Dependency case class with 3 fields: taskID, and dependencyTaskID
// TODO consider renaming to ancestor and child
case class Dependency(id: Int = 0, taskID: Int, dependencyTaskID: Int)

// Table for dependencies
class Dependencies(tag: Tag)
  extends Table[Dependency](tag, "DEPENDENCIES") {

  // Column for ID, primary key and autoinc
  def id: Rep[Int] = column[Int]("DEPENDENCY_ID", O.PrimaryKey, O.AutoInc)

  // column for the task ID this dependency is for
  def taskID: Rep[Int] = column[Int]("TASK_ID")

  // column for the task ID of the depdendency task (task that is depdendent to the one above)
  def dependencyTaskID: Rep[Int] = column[Int]("DEPENDENCY_TASK_ID")

  // Index to ensure we are not inserting the same dependency twice over.
  def compositeKey = index("DEPENDENCY_COMPOSITE_KEY", (taskID, dependencyTaskID), unique = true)

  // * Projection
  def * = (id, taskID, dependencyTaskID) <> (Dependency.tupled, Dependency.unapply)

}

// Object for table
object Tables {

  // Creating an interface for TaskTopics
  lazy val categories: TableQuery[Categories] = TableQuery[Categories]

  // Creating an interface for Tasks
  lazy val tasks: TableQuery[Tasks] = TableQuery[Tasks]

  // Creating an interface for Dependencies
  lazy val dependencies: TableQuery[Dependencies] = TableQuery[Dependencies]

}