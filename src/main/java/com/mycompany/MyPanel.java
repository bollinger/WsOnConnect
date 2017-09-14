package com.mycompany;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.event.WebSocketClosedPayload;
import org.apache.wicket.protocol.ws.api.event.WebSocketConnectedPayload;
import org.apache.wicket.protocol.ws.api.event.WebSocketPushPayload;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;

/**
 * A panel with a link. The link should only be enabled while there is a web socket connection.
 *
 * Created by peter on 14/09/17.
 */
public class MyPanel extends Panel {

  private ConnectedMessage connectedMessage = null;

  private AjaxLink link = null;

  MyPanel(String id) {
    super(id);
  }

  private boolean webSocketConnected() {
    return connectedMessage != null;
  }

  public void onInitialize() {
    super.onInitialize();

    add(new WebSocketBehavior() { });


    link = new AjaxLink("pressMe") {
      @Override
      protected void onInitialize() {
        super.onInitialize();
      }

      @Override
      protected void onConfigure() {
        super.onConfigure();
        println("Link onConfigure webSocketConnected() = " + webSocketConnected() );
        setEnabled( webSocketConnected() );
      }

      @Override
      public void onClick(AjaxRequestTarget ajaxRequestTarget) {
        println("Press Me was Pressed.!");
      }
    };
    link.setOutputMarkupId(true);
    add(link);



    AjaxLink refresh = new AjaxLink("refresh") {
      @Override
      public void onClick(AjaxRequestTarget art) {
        art.add(link);
      }
    };
    add(refresh);
  }







  private void println(String str) {
    System.out.println(str);
  }


  private void onWebSocketEvent(WebSocketPushPayload wsEvent) {
    println("onWebSocketEvent" + wsEvent);
  }



  public void onEvent(IEvent event) {
    super.onEvent(event);

    Object payload = event.getPayload();

    if (payload instanceof WebSocketPushPayload) {
      onWebSocketEvent((WebSocketPushPayload)payload);

    } else if (payload instanceof WebSocketConnectedPayload) {
      println("onEvent wsConnect: WebSocketConnectedPayload");
      WebSocketConnectedPayload wsConnect = (WebSocketConnectedPayload)payload;
      connectedMessage = wsConnect.getMessage(); // this should enable the link.
      wsConnect.getHandler().add(link); // <--- seems this is not calling onConfigure on the link. so link is not enabled.

    } else if (payload instanceof WebSocketClosedPayload) {
      println("onEvent wsConnect: WebSocketClosedPayload");
      WebSocketClosedPayload wsDisconnect = (WebSocketClosedPayload)payload;
      connectedMessage = null;
      wsDisconnect.getHandler().add(link);

    } else {
      println("Some other event " + payload);
    }

  }
}
