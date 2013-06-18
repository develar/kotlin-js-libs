package wip.dom

public optionsArg class RGBA(public val r:Int, public val g:Int, public val b:Int, public val a:Double = 1.0)
public optionsArg class HighlightNodeMessage(public val nodeId:Int, public val highlightConfig:Any)
public optionsArg class ResolveNodeMessage(public val nodeId:Int)
public optionsArg class RequestChildNodesMessage(public val nodeId:Int)
public optionsArg class RequestNodeMessage(public val objectId:String)
public optionsArg class SetOuterHTMLMessage(public val nodeId:Int, public val outerHTML:String)
public optionsArg class SetNodeValueMessage(public val nodeId:Int, public val value:String)
public optionsArg class SetAttributeValueMessage(public val nodeId:Int, public val name:String, public val value:String)
public optionsArg class QuerySelectorMessage(public val nodeId:Int, public val selector:String)