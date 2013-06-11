package wip.css

public native optionsArg class CssStyleId(public val styleSheetId:String, public val ordinal:Int)

public native optionsArg class GetInlineStylesForNodeMessage(public val nodeId:Int)
public native optionsArg class GetStyleSheetMessage(public val styleSheetId:String)
public native optionsArg class SetStyleSheetTextMessage(public val styleSheetId:String, public val text:String)
public native optionsArg class SetPropertyTextMessage(public val styleId:CssStyleId, public val propertyIndex:Int, public val text:String, public val overwrite:Boolean)