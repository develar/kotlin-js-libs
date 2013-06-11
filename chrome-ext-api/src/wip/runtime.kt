package wip.runtime

public native trait RemoteObject {
  public val objectId:String?
  public val value:Any?
}

public native trait CallArgument {
  public val objectId:String
}

public native optionsArg class CallFunctionOnMessage(public val objectId:String, public val functionDeclaration:String, returnByValue:Boolean = false)
public native optionsArg class ReleaseObjectMessage(public val objectId:String)
public native optionsArg class ReleaseObjectGroupMessage(public val objectGroup:String)