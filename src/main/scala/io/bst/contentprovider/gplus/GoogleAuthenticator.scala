package io.bst.contentprovider.gplus

import akka.event.LoggingAdapter
import java.util.UUID
import spray.http.Uri


trait GoogleAuthenticator {
  this: LoggingAdapter =>

  def authenticate() = {

    val clientId = "914945663646-p00979sopuk3nv4v7bi8ek8359oppd3k.apps.googleusercontent.com"
    val clientSecret = "kCVASP8wkHq6y6eFEq_iYPLT"
    val state = UUID.randomUUID().toString
    val params = Map("client_id" -> clientId,
      "response_type" -> "code",
      "scope" -> "https://www.googleapis.com/auth/plus.login",
      "redirect_uri" -> "http://127.0.0.1:8080/oauth/cb/google",
      "state" -> state,
      "access_type" -> "offline")
    val uri = Uri("https://accounts.google.com/o/oauth2/auth?" + params.map {
      case (key, value) => s"$key=$value"
    }.mkString("&"))

  }
}
