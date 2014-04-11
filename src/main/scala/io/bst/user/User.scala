package io.bst.user

import java.util.{Optional, UUID}

object User {
  val testUser = User(UUID.randomUUID(), "test", "foo@bar.com")
}

/**
 * A register user of BST. This class is used to enable multi-tenancy.
 * @author Harald Pehl
 */
case class User(id: UUID, username: String, email: String, firstname: Optional[String] = None, surname: Optional[String] = None)
