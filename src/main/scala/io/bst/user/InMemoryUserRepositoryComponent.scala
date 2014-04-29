package io.bst.user

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._
import scala.util.Random


class InMemoryUserRepositoryComponent extends UserRepositoryComponent {

  override def userRepository: UserRepository = new InMemoryUserRepository

  class InMemoryUserRepository extends UserRepository {
    val users = new ConcurrentHashMap[String, User].asScala

    override def find(username: String): Option[User] =
      Some(users.getOrElse(username, {
        // register on the fly ;-)
        val email = s"$username@${Random.alphanumeric.take(5).mkString}.com"
        val user = User(username, email)
        users += (username -> user)
        user
      }))
  }

}
