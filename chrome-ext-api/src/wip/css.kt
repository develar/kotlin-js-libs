package wip.css

public optionsArg class CssStyleId(public val styleSheetId:String, public val ordinal:Int)

public optionsArg class GetInlineStylesForNodeMessage(public val nodeId:Int)
public optionsArg class GetStyleSheetMessage(public val styleSheetId:String)
public optionsArg class SetStyleSheetTextMessage(public val styleSheetId:String, public val text:String)
public optionsArg class SetPropertyTextMessage(public val styleId:CssStyleId, public val propertyIndex:Int, public val text:String, public val overwrite:Boolean)

public class CssStyle(public val styleId:CssStyleId, public val cssProperties:Array<CssProperty>)

public class CssProperty(public val name:String, public val value:String, public val parsedOk:Boolean?, public val text:String?)

public trait CssStyleSheetHeader {
  public val styleSheetId:String
  public val sourceURL:String
  public val title:String
  public val disabled:Boolean
  public val origin:String
}

public trait CssStyleSheetBody {
  public val styleSheetId:String
  public val rules:Array<CssRule>
  public val text:String
}

public trait CssRule {
  public val ruleId:Int
  public val selectorText:String
  public val sourceURL:String
  public val sourceLine:Int
  public val origin:String
  public val style:CssStyle
}
