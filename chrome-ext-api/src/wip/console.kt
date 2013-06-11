package wip.console

import wip.runtime.RemoteObject
import wip.debugger.CallFrame

public native trait ConsoleMessage {
  public val level:String
  public val text:String
  public val url:String?
  public val line:Int?

  public val parameters:Array<RemoteObject>?
  public val stackTrace:Array<CallFrame>?

  public val `type`:String
}