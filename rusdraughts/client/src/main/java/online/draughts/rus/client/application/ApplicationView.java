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

package online.draughts.rus.client.application;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * This is the top-level view of the application. Every time another presenter wants to reveal itself, {@link
 * ApplicationView} will add its content of the target inside the {@code mainContantPanel}.
 */
public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

  private final Widget widget;

  @UiField
  SimplePanel menuPanel;
  @UiField
  SimplePanel mainContentPanel;
  @Inject
  ApplicationView(
      Binder binder) {
    widget = binder.createAndBindUi(this);

    bindSlot(ApplicationPresenter.SLOT_MENU, menuPanel);
    bindSlot(ApplicationPresenter.SLOT_MAIN_CONTENT, mainContentPanel);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  public interface Binder extends UiBinder<Widget, ApplicationView> {
  }
}
