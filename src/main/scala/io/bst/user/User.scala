package io.bst.user

/**
 * A register user of BST. This class is used to enable multi-tenancy.
 * @author Harald Pehl
 */
case class User(username: String, email: String, firstName: Option[String] = None, surname: Option[String] = None)
