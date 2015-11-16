package online.draughts.rus.client.application.home;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwtmockito.WithClassesToStub;
import online.draughts.rus.CustomGwtMockitoTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 10:57
 */
@RunWith(CustomGwtMockitoTestRunner.class)
@WithClassesToStub(RootPanel.class)
public class HomeViewTest {

  private PlayComponentView playComponentView;
  private PlayComponentPresenter playComponentPresenter;

  @Test
  public void testHomeView() {
//    PlayComponentView.Binder playBinder = GWT.create(PlayComponentView.Binder.class);
//    DraughtsMessages messages = GWT.create(DraughtsMessages.class);
//    AppResources resources = GWT.create(AppResources.class);
//    playComponentView = new PlayComponentView(playBinder, messages, resources);
//    assertNotNull(playComponentView);

//    SimpleEventBus eventBus = new SimpleEventBus();
//    ResourceDelegate<GamesResource> gamesResource = GWT.create(ResourceDelegate.class);
//    ResourceDelegate<PlayersResource> playersResource = GWT.create(ResourceDelegate.class);
//    ResourceDelegate<FriendsResource> friendsResource = GWT.create(ResourceDelegate.class);
//    CurrentSession currentSession = GWT.create(CurrentSession.class);
//    PlaySession playSession = GWT.create(PlaySession.class);
//    ClientConfiguration config = GWT.create(ClientConfiguration.class);
//    GameMessageMapper mapper = GWT.create(GameMessageMapper.class);
//    ClientWebsocket clientWebsocket = new ClientWebsocket(eventBus, currentSession, playSession, config, mapper, gamesResource,
//        messages);
//    assertNotNull(clientWebsocket);
//
//    playComponentPresenter = new PlayComponentPresenter(eventBus, playComponentView, messages,
//        gamesResource, playersResource, friendsResource, clientWebsocket);
//    assertNotNull(playComponentPresenter);
//
//    playComponentView.onConnectToServer(null);
//    Player opponent = playComponentView.playerCellTable.getVisibleItem(0);
//    assertNotNull(opponent);
  }

//  public static class Module extends ViewTestModule {
//
//    @Override
//    protected void configureViewTest() {
//      MockitoAnnotations.initMocks(this);
//
//      forceMock(UIObject.class);
//
//      bind(HomeView.Binder.class).to(HomeTestBinder.class);
//      bind(PlayShowPanel.Binder.class).to(PlayShowPanelTestBinder.class);
//      bind(PlayComponentView.Binder.class).to(PlayComponentViewTestBinder.class);
//    }
//
//    static class HomeTestBinder extends MockingBinder<Widget, HomeView> implements HomeView.Binder {
//
//      @Inject
//      public HomeTestBinder(final MockFactory mockitoMockFactory) {
//        super(Widget.class, mockitoMockFactory);
//      }
//    }
//
//    static class PlayShowPanelTestBinder extends MockingBinder<HTMLPanel, PlayShowPanel> implements PlayShowPanel.Binder {
//
//      @Inject
//      public PlayShowPanelTestBinder(MockFactory mockFactory) {
//        super(HTMLPanel.class, mockFactory);
//      }
//    }
//
//    static class PlayComponentViewTestBinder extends MockingBinder<Widget, PlayComponentView> implements PlayComponentView.Binder {
//
//      @Inject
//      public PlayComponentViewTestBinder(MockFactory mockFactory) {
//        super(Widget.class, mockFactory);
//      }
//    }
//
////    public class Resources implements AbstractCellTable.Resources {
////      /**
////       * The background used for footer cells.
////       */
////      ImageResource cellTableFooterBackground() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      /**
////       * The background used for header cells.
////       */
////      ImageResource cellTableHeaderBackground() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      /**
////       * The loading indicator used while the table is waiting for data.
////       */
////      ImageResource cellTableLoading() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      /**
////       * The background used for selected cells.
////       */
////      ImageResource cellTableSelectedBackground() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      /**
////       * Icon used when a column is sorted in ascending order.
////       */
////      ImageResource cellTableSortAscending() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      /**
////       * Icon used when a column is sorted in descending order.
////       */
////      ImageResource cellTableSortDescending() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      /**
////       * The styles used in this widget.
////       */
////      CellTable.Style cellTableStyle() {
////        return new StyleB3();
////      }
////
////      @Override
////      public ImageResource sortAscending() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      @Override
////      public ImageResource sortDescending() {
////        return new ImageResourcePrototype("test", null, 0, 0, 0, 0, false, false);
////      }
////
////      @Override
////      public AbstractCellTable.Style style() {
////        return new Style();
////      }
////    }
//
////    /**
////     * Styles used by this widget.
////     */
////    public class Style implements AbstractCellTable.Style{
////      /**
////       * Applied to every cell.
////       */
////      public String cell() {
////        return "cell";
////      }
////
////      /**
////       * Applied to even rows.
////       */
////      public String evenRow() {
////        return "evenRow";
////      }
////
////      /**
////       * Applied to cells in even rows.
////       */
////      public String evenRowCell() {
////        return "evenRowCell";
////      }
////
////      /**
////       * Applied to the first column.
////       */
////      public String firstColumn() {
////        return "firstColumn";
////      }
////
////      /**
////       * Applied to the first column footers.
////       */
////      public String firstColumnFooter() {
////        return "firstColumnFooter";
////      }
////
////      /**
////       * Applied to the first column headers.
////       */
////      public String firstColumnHeader() {
////        return "firstColumnHeader";
////      }
////
////      /**
////       * Applied to footers cells.
////       */
////      public String footer() {
////        return "footer";
////      }
////
////      /**
////       * Applied to headers cells.
////       */
////      public String header() {
////        return "header";
////      }
////
////      /**
////       * Applied to the hovered row.
////       */
////      public String hoveredRow() {
////        return "hoveredRow";
////      }
////
////      /**
////       * Applied to the cells in the hovered row.
////       */
////      public String hoveredRowCell() {
////        return "hoveredRowCell";
////      }
////
////      /**
////       * Applied to the keyboard selected cell.
////       */
////      public String keyboardSelectedCell() {
////        return "keyboardSelectedCell";
////      }
////
////      /**
////       * Applied to the keyboard selected row.
////       */
////      public String keyboardSelectedRow() {
////        return "keyboardSelectedRow";
////      }
////
////      /**
////       * Applied to the cells in the keyboard selected row.
////       */
////      public String keyboardSelectedRowCell() {
////        return "keyboardSelectedRowCell";
////      }
////
////      /**
////       * Applied to the last column.
////       */
////      public String lastColumn() {
////        return "lastColumn";
////      }
////
////      /**
////       * Applied to the last column footers.
////       */
////      public String lastColumnFooter() {
////        return "lastColumnFooter";
////      }
////
////      /**
////       * Applied to the last column headers.
////       */
////      public String lastColumnHeader() {
////        return "lastColumnHeader";
////      }
////
////      /**
////       * Applied to odd rows.
////       */
////      public String oddRow() {
////        return "oddRow";
////      }
////
////      /**
////       * Applied to cells in odd rows.
////       */
////      public String oddRowCell() {
////        return "oddRowCell";
////      }
////
////      /**
////       * Applied to selected rows.
////       */
////      public String selectedRow() {
////        return "selectedRow";
////      }
////
////      /**
////       * Applied to cells in selected rows.
////       */
////      public String selectedRowCell() {
////        return "selectedRowCell";
////      }
////
////      /**
////       * Applied to header cells that are sortable.
////       */
////      public String sortableHeader() {
////        return "sortableHeader";
////      }
////
////      /**
////       * Applied to header cells that are sorted in ascending order.
////       */
////      public String sortedHeaderAscending() {
////        return "sortedHeaderAscending";
////      }
////
////      /**
////       * Applied to header cells that are sorted in descending order.
////       */
////      public String sortedHeaderDescending() {
////        return "sortedHeaderDescending";
////      }
////
////      /**
////       * Applied to the table.
////       */
////      public String widget() {
////        return "widget";
////      }
////    }
//
////    /**
////     * Styles used by this widget.
////     */
////    public class StyleB3 implements CellTable.Style {
////      /**
////       * The path to the default CSS styles used by this resource.
////       */
//////      String DEFAULT_CSS = "com/google/gwt/user/cellview/client/CellTable.css";
////
////      /**
////       * Applied to every cell.
////       */
////      public String cellTableCell() { return "cellTableCell"; }
////
////      /**
////       * Applied to even rows.
////       */
////      public String cellTableEvenRow() { return "cellTableEvenRow"; }
////
////      /**
////       * Applied to cells in even rows.
////       */
////      public String cellTableEvenRowCell() { return "cellTableEvenRowCell"; }
////
////      /**
////       * Applied to the first column.
////       */
////      public String cellTableFirstColumn() { return "cellTableFirstColumn"; }
////
////      /**
////       * Applied to the first column footers.
////       */
////      public String cellTableFirstColumnFooter() { return "cellTableFirstColumnFooter"; }
////
////      /**
////       * Applied to the first column headers.
////       */
////      public String cellTableFirstColumnHeader() { return "cellTableFirstColumnHeader"; }
////
////      /**
////       * Applied to footers cells.
////       */
////      public String cellTableFooter() { return "cellTableFooter"; }
////
////      /**
////       * Applied to headers cells.
////       */
////      public String cellTableHeader() { return "cellTableHeader"; }
////
////      /**
////       * Applied to the hovered row.
////       */
////      public String cellTableHoveredRow() { return "cellTableHoveredRow"; }
////
////      /**
////       * Applied to the cells in the hovered row.
////       */
////      public String cellTableHoveredRowCell() { return "cellTableHoveredRowCell"; }
////
////      /**
////       * Applied to the keyboard selected cell.
////       */
////      public String cellTableKeyboardSelectedCell() { return "cellTableKeyboardSelectedCell"; }
////
////      /**
////       * Applied to the keyboard selected row.
////       */
////      public String cellTableKeyboardSelectedRow() { return "cellTableKeyboardSelectedRow"; }
////
////      /**
////       * Applied to the cells in the keyboard selected row.
////       */
////      public String cellTableKeyboardSelectedRowCell() { return "cellTableKeyboardSelectedRowCell"; }
////
////      /**
////       * Applied to the last column.
////       */
////      public String cellTableLastColumn() { return "cellTableLastColumn"; }
////
////      /**
////       * Applied to the last column footers.
////       */
////      public String cellTableLastColumnFooter() { return "cellTableLastColumnFooter"; }
////
////      /**
////       * Applied to the last column headers.
////       */
////      public String cellTableLastColumnHeader() { return "cellTableLastColumnHeader"; }
////
////      /**
////       * Applied to the loading indicator.
////       */
////      public String cellTableLoading() { return "cellTableLoading"; }
////
////      /**
////       * Applied to odd rows.
////       */
////      public String cellTableOddRow() { return "cellTableOddRow"; }
////
////      /**
////       * Applied to cells in odd rows.
////       */
////      public String cellTableOddRowCell() { return "cellTableOddRowCell"; }
////
////      /**
////       * Applied to selected rows.
////       */
////      public String cellTableSelectedRow() { return "cellTableSelectedRow"; }
////
////      /**
////       * Applied to cells in selected rows.
////       */
////      public String cellTableSelectedRowCell() { return "cellTableSelectedRowCell"; }
////
////      /**
////       * Applied to header cells that are sortable.
////       */
////      public String cellTableSortableHeader() { return "cellTableSortableHeader"; }
////
////      /**
////       * Applied to header cells that are sorted in ascending order.
////       */
////      public String cellTableSortedHeaderAscending() { return "cellTableSortedHeaderAscending"; }
////
////      /**
////       * Applied to header cells that are sorted in descending order.
////       */
////      public String cellTableSortedHeaderDescending() { return "cellTableSortedHeaderDescending"; }
////
////      /**
////       * Applied to the table.
////       */
////      public String cellTableWidget() { return "cellTableWidget"; }
////
////      @Override
////      public boolean ensureInjected() {
////        return true;
////      }
////
////      @Override
////      public String getText() {
////        return "text";
////      }
////
////      @Override
////      public String getName() {
////        return "name";
////      }
////    }
//  }
}