package com.seb.model

import java.util.UUID

case class ServerGetList(servers: List[ServerGet])

case class ServerGet(id:Int, name:String)

case class ServerPost(name: String)

case class User(userId: Int, canListServers: Boolean, canDeleteServer: Boolean = true)
