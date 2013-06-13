package chrome

import java.util.ArrayList

// http://src.chromium.org/viewvc/chrome/trunk/src/chrome/common/extensions/api/

public trait Node {
  public val nodeId:Int
  public val nodeType:Int
  public val children:Array<Node>?
}

public trait ChromeEvent<T> {
  public fun addListener(callback:T, filter:Any? = null, extraInfoSpec:Array<String>? = null)
  public fun removeListener(callback:T)

  public fun addRules(rules:Array<Any>, callback:(()->Unit)? = null)
}

public trait MessageEvent : ChromeEvent<(message:Any)->Unit> {
  public fun addListener<T>(callback:(message:T)->Any?)
  override fun removeListener(callback:(message:Any)->Unit)
}

public trait Port {
  public val name:String
  public val onDisconnect:ChromeEvent<(port:Port)->Unit>
  public val onMessage:MessageEvent
  public fun postMessage(message:Any):Unit = noImpl
}

private open class PortImpl(override public val name:String) : Port {
  override val onMessage:MessageEvent = noImpl
  override val onDisconnect:ChromeEvent<(port:Port)->Unit> = noImpl
}

public class PortOnConnect(name:String, public val sender:MessageSender) : PortImpl(name)

public trait MessageSender {
  public val tab:Tab?
  public val id:String?
}

public optionsArg class Debuggee (public val tabId:Int)

public trait Debugger {
  // reason since chrome 22
  public val onDetach:ChromeEvent<(debuggee:Debuggee, reason:String? = null)->Any?>
  public val onEvent:ChromeEvent<(debuggee:Debuggee, method:String, data:Json)->Any?>

  public fun sendCommand<T>(target:Debuggee, method:String, params:Any?, callback:((result:T)->Unit)? = null)

  public fun attach(target:Debuggee, protocol:String, callback:(()->Unit)? = null)
  public fun detach(target:Debuggee, callback:(()->Unit)? = null)
}

public trait Window {
  public val id:Int

  public val top:Int
  public val left:Int

  public val height:Int
  public val width:Int

  public val focused:Boolean
  public val alwaysOnTop:Boolean
  public val incognito:Boolean

  public val state:String

  public val tabs:Array<Tab>?

  public val `type`:String
}

public trait Tab {
  public val url:String
  public val id:Int
  public val windowId:Int
  public val incognito:Boolean

  public val status:String?
}

public trait ChangeInfo {
  public val url:String?
  public val status:String?
  public val pinned:Boolean?
}

public trait Windows {
  public fun getCurrent(properties:Any? = null, callback:(window:Window?)->Unit)
  public fun getLastFocused(properties:Any? = null, callback:(window:Window?)->Unit)
  public fun getAll(properties:Any? = null, callback:(window:Array<Window>)->Unit)

  public optionsArg fun create(url:String? = null, tabId:Int? = null, focused:Boolean? = null, incognito:Boolean? = null, callback:((window:Window)->Unit)? = null)
  public optionsArg fun update(windowId:Int, focused:Boolean? = null, drawAttention:Boolean? = null, callback:((window:Window)->Unit)? = null)
}

public trait Tabs {
  public val onUpdated:ChromeEvent<(tabId:Int, changeInfo:ChangeInfo, tab:Tab)->Unit>
  public val onRemoved:ChromeEvent<(tabId:Int, removeInfo:Any)->Any?>

  public fun get(tabId:Int, callback:(tab:Tab)->Unit)

  public fun query(query:Any, callback:(tabs:Array<Tab>)->Unit)

  public fun sendRequest<T>(tabId:Int, request:Any, callback:(response:T)->Unit)
  public fun sendRequest(tabId:Int, request:Any)

  public fun reload(tabId:Int, optionsArg bypassCache:Boolean = false, callback:(()->Unit)? = null)

  public optionsArg fun create(url:String? = null, windowId:Int? = null, index:Int? = null, active:Boolean? = null, pinned:Boolean? = null, openerTabId:Int? = null, callback:((tab:Tab)->Unit)? = null)
  public optionsArg fun update(tabId:Int, url:String? = null, active:Boolean? = null, highlighted:Boolean? = null, pinned:Boolean? = null, openerTabId:Int? = null, callback:((tab:Tab)->Unit)? = null)
}

public trait LastError {
  public val message:String
}

public native trait Extension {
  deprecated("use chrome.runtime.lastError")
  public val lastError:LastError?
  public val onRequest:ChromeEvent<(request:Any, sender:Any, sendResponse:(response:Any)->Unit)->Unit>
  public val onConnect:ChromeEvent<(port:PortOnConnect)->Any>

  public fun sendRequest<T>(extensionId:String?, request:Any, callback:((response:T)->Unit)?)
  public fun sendRequest<T>(request:Any, callback:((response:T)->Unit)? = null)
  public fun sendRequest(request:Any)

  deprecated("use chrome.runtime.getURL")
  public fun getURL(path:String):String
}

public trait App {
  // http://developer.chrome.com/trunk/apps/app.runtime.html
  public trait Runtime {
    public trait LaunchData {
      public val id:String?
      public val items:Array<Any>?
    }

    // http://developer.chrome.com/apps/app.window.html
    public trait WindowApi {
      public trait AppWindow {
        public val contentWindow:Any
      }

      public fun create(url:String, options:Any? = null, callback:((window:AppWindow)->Unit)? = null)
    }

    public val onLaunched:ChromeEvent<(data:LaunchData? = null)->Unit>
  }

  public fun getDetails():AppDetails
  public val runtime:Runtime
  public val window:Runtime.WindowApi
}

public trait AppDetails {
  public val version:String
}

public class StorageChange(val oldValue:Any, val newValue:Any)

public trait Storage {
  public val local:StorageArea
  public val sync:StorageArea

  public val onChanged:ChromeEvent<(changes:Map<String, StorageChange>, areaName:String)->Unit>
}

public trait StorageArea {
  native("get")
  public fun _get<T>(key:String?, callback:(data:T)->Unit):Unit

  public fun set(items:Any, callback:(()->Unit)? = null):Unit
}

public trait SuggestResult {

}

public trait Omnibox {
  public val onInputChanged:ChromeEvent<(text:String, suggest:((suggestResults:Array<SuggestResult>)->Unit)? = null)->Any>
}

public trait BrowserAction {
  public val onClicked:ChromeEvent<(tab:Tab)->Unit>

  public fun disable(tabId:Int? = null)
  public fun enable(tabId:Int? = null)

  public optionsArg fun setTitle(optionsArg title:String, tabId:Int? = null)

  public optionsArg class ImagePath(val `19`:String, val `38`:String)

  // you should use chrome jb extension method
  public optionsArg fun setIcon(imageData:Any? = null, path:ImagePath? = null, tabId:Int? = null, callback:(()->Unit)? = null)
}

public trait WebRequest {
  public trait RequestDetails {
    public val requestId:String
    public val url:String
    public val method:String
    public val tabId:Int
  }

  public trait BeforeRequestDetails : RequestDetails

  public optionsArg class RequestFilter(public val urls:Array<String>, types:Array<String>? = null, tabId:Int? = null, windowId:Int? = null)
  public optionsArg class HttpHeader(public val name:String, public var value:String?)
  public optionsArg class BlockingResponse(public val responseHeaders:Array<HttpHeader>? = null, public redirectUrl:String? = null)

  public trait HeadersReceivedDetails : RequestDetails {
    public val responseHeaders:ArrayList<HttpHeader>?
  }

  public trait BeforeSendHeadersDetails : RequestDetails {
    public val requestHeaders:ArrayList<HttpHeader>?
  }

  public trait CompletedDetails : RequestDetails {
    public val fromCache:Boolean
    public val statusCode:Int
  }

  public trait ErrorDetails : RequestDetails {
    public val fromCache:Boolean
    public val error:String
  }

  public val onBeforeRequest:ChromeEvent<(details:BeforeRequestDetails)->Any?>
  public val onBeforeSendHeaders:ChromeEvent<(details:BeforeSendHeadersDetails)->Any?>
  public val onHeadersReceived:ChromeEvent<(details:HeadersReceivedDetails)->Any?>
  public val onCompleted:ChromeEvent<(details:CompletedDetails)->Unit>
  public val onErrorOccurred:ChromeEvent<(details:ErrorDetails)->Unit>

  public fun handlerBehaviorChanged(callback:(()->Unit)? = null)
}

public fun createRequestMatcher(vararg p:Any?):Any = noImpl
public fun createRedirectRequest(url:String):Any = noImpl

public trait DeclarativeWebRequest {
  public val onRequest:ChromeEvent<()->Unit>
}

public object chrome {
  public val debugger:Debugger
  public val tabs:Tabs
  public val windows:Windows
  public val extension:Extension
  public val app:App
  public val storage:Storage
  public val omnibox:Omnibox
  public val browserAction:BrowserAction
  public val webRequest:WebRequest
  public val declarativeWebRequest:DeclarativeWebRequest

  public object cookies {
    public trait Cookie {
      public val name:String
      public val value:String
      public val domain:String
      public val path:String
      public val secure:Boolean
      public val httpOnly:Boolean
      public val hostOnly:Boolean
      public val session:Boolean
      public val expirationDate:Double
      public val storeId:String
    }

    public fun get(query:Any, callback:(cookie:Cookie)->Unit):Unit
    public fun getAll(query:Any, callback:(cookies:Array<Cookie>)->Unit):Unit
  }

  public object runtime {
    public val lastError:LastError?
    public val id:String
    public fun getURL(path:String):String
    public fun reload():Unit
  }
}