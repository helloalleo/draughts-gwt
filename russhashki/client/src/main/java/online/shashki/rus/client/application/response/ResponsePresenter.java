/*
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package online.shashki.rus.client.application.response;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.shared.dispatch.SendTextToServerAction;
import online.shashki.rus.shared.dispatch.SendTextToServerResult;

import javax.inject.Inject;

public class ResponsePresenter
        extends Presenter<ResponsePresenter.MyView, ResponsePresenter.MyProxy>
        implements ResponseUiHandlers {
    @ProxyCodeSplit
    @NameToken(NameTokens.response)
    @NoGatekeeper
    interface MyProxy extends ProxyPlace<ResponsePresenter> {
    }

    interface MyView extends View, HasUiHandlers<ResponseUiHandlers> {
        void setServerResponse(String serverResponse);

        void setTextToServer(String textToServer);
    }

    private final DispatchAsync dispatcher;
    private final PlaceManager placeManager;

    private String textToServer;

    @Inject
    ResponsePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            DispatchAsync dispatcher) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        this.dispatcher = dispatcher;

        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        textToServer = "HI";
    }

    @Override
    public void onClose() {
        PlaceRequest homePlaceRequest = new PlaceRequest.Builder().nameToken(NameTokens.homePage).build();
        placeManager.revealPlace(homePlaceRequest);
    }

    @Override
    protected void onReset() {
        super.onReset();

        getView().setTextToServer(textToServer);
        getView().setServerResponse("Waiting for response...");

        dispatcher.execute(new SendTextToServerAction(textToServer), new AsyncCallback<SendTextToServerResult>() {
            @Override
            public void onFailure(Throwable caught) {
                getView().setServerResponse("An error occured: " + caught.getMessage());
            }

            @Override
            public void onSuccess(SendTextToServerResult result) {
                getView().setServerResponse(result.getResponse());
            }
        });
    }
}
