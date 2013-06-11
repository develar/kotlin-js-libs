package wip.dom

public native optionsArg class RGBA(public val r:Int, public val g:Int, public val b:Int, public val a:Double = 1.0)
public native optionsArg class HighlightNodeMessage(public val nodeId:Int, public val highlightConfig:Any)
public native optionsArg class ResolveNodeMessage(public val nodeId:Int)
public native optionsArg class RequestChildNodesMessage(public val nodeId:Int)
public native optionsArg class RequestNodeMessage(public val objectId:String)
public native optionsArg class SetOuterHTMLMessage(public val nodeId:Int, public val outerHtml:String)
public native optionsArg class SetNodeValueMessage(public val nodeId:Int, public val value:String)
public native optionsArg class SetAttributeValueMessage(public val nodeId:Int, public val name:String, public val value:String)
public native optionsArg class QuerySelectorMessage(public val nodeId:Int, public val selector:String)