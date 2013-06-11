package wip.debugger

public native optionsArg class SetScriptSourceMessage(public val scriptId:String, public val source:String)

public native trait ScriptParsedData : Json {
  public val scriptId:String
  public val url:String
  public val isContentScript:Boolean?
  public val sourceMapURL:String?

  public val startLine:Int
  public val startColumn:Int
  public val endLine:Int
  public val endColumn:Int

  public val hasSourceURL:Boolean?
}

public native trait CallFrame {
  public val url:String
  public val lineNumber:Int
  public val columnNumber:Int
  public val functionName:String
}

public native trait ScriptPausedData {
  public val callFrames:Array<CallFrame>
  public val reason:String
  public val data:Any?
}