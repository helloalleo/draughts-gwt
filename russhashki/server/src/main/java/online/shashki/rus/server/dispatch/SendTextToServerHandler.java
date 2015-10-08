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

package online.shashki.rus.server.dispatch;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import online.shashki.rus.shared.dispatch.SendTextToServerAction;
import online.shashki.rus.shared.dispatch.SendTextToServerResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SendTextToServerHandler implements ActionHandler<SendTextToServerAction, SendTextToServerResult> {
    private Provider<HttpServletRequest> requestProvider;
    private ServletContext servletContext;

    @Inject
    SendTextToServerHandler(
        ServletContext servletContext,
        Provider<HttpServletRequest> requestProvider) {
        this.servletContext = servletContext;
        this.requestProvider = requestProvider;
    }

    @Override
    public SendTextToServerResult execute(SendTextToServerAction action, ExecutionContext context)
            throws ActionException {
        String input = action.getTextToServer();

        // Verify that the input is valid.
        String serverInfo = servletContext.getServerInfo();
        String userAgent = requestProvider.get().getHeader("User-Agent");
        String response = String.format("Hello, %s!<br/><br/>I am running %s.<br/><br/>" +
                        "It looks like you are using:<br/>%s",
                input, serverInfo, userAgent);

        return new SendTextToServerResult(response);
    }

    @Override
    public Class<SendTextToServerAction> getActionType() {
        return SendTextToServerAction.class;
    }

    @Override
    public void undo(SendTextToServerAction action, SendTextToServerResult result, ExecutionContext context)
            throws ActionException {
        // Not undoable
    }
}
