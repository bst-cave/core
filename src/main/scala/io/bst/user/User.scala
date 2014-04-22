package io.bst.user

import java.util.UUID

/**
 * A register user of BST. This class is used to enable multi-tenancy.
 * @author Harald Pehl
 */
case class User(id: UUID, username: String, email: String, firstName: Option[String] = None, surname: Option[String] = None)
