package io.bst.user


trait UserRepositoryComponent {

  def userRepository: UserRepository

  trait UserRepository {
    def find(username: String): Option[User]
  }

}