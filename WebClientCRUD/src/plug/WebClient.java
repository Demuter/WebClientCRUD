package plug;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import java.util.ArrayList;
import java.util.List;
import web.LoadWeb;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

public class WebClient {

	protected static Shell shlWebclientcrud;
	private Text textName;
	private Text textNo;
	private Text textPosit;
	private Text text;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			WebClient window = new WebClient();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
			MessageBox messageBox = new MessageBox(shlWebclientcrud, SWT.ICON_ERROR | SWT.OK);
			messageBox.setMessage("You did not choose a method!" + "\r\n" + "Please choose a method!");
			messageBox.setText("Error");
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlWebclientcrud.open();
		shlWebclientcrud.layout();
		while (!shlWebclientcrud.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlWebclientcrud = new Shell();
		shlWebclientcrud.setSize(450, 450);
		shlWebclientcrud.setText("WebClientCRUD");
		shlWebclientcrud.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shlWebclientcrud, SWT.NONE);

		String[] items = new String[] { "GET", "POST", "DEL" };

		RowLayout rl_composite = new RowLayout(SWT.VERTICAL);
		rl_composite.fill = true;
		composite.setLayout(rl_composite);

		Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setItems(items);
		combo.select(0);
		combo.setText("GET");

		textName = new Text(composite, SWT.BORDER);
		textName.setText("Enter name");

		textNo = new Text(composite, SWT.BORDER);
		textNo.setText("Enter number");

		textPosit = new Text(composite, SWT.BORDER);
		textPosit.setText("Enter position");

		Button buttonSent = new Button(composite, SWT.NONE);
		buttonSent.setText("Sent");

		textName.setEditable(false);
		textNo.setEditable(false);
		textPosit.setEditable(false);

		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setLayoutData(new RowData(398, 256));
		text.setText(
				"Hit the Send button to get a response. \r\nTo add an element, enter the name, number and position with a space. \r\nTo delete an item, enter a user number. ");

		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				int idx = combo.getSelectionIndex();
				String action = combo.getItem(idx);
				System.out.println("combo " + idx);
				System.out.println("combo " + action);

				if (action.equals("GET")) {
					textName.setEditable(false);
					textNo.setEditable(false);
					textPosit.setEditable(false);
					text.setText("You have chosen the GET method.");

				} else if (action.equals("POST")) {
					textName.setEditable(true);
					textNo.setEditable(true);
					textPosit.setEditable(true);
					text.setText("You have chosen the POST method. \r\n"
							+ "Please enter user information to add them to the server. \r\n"
							+ "Example for input data: \r\n" + "\t \"empName\":\"Smith\" \r\n"
							+ "\t \"empNo\":\"E01\" \r\n" + "\t \"position\":\"Clerk\" \r\n"
							+ "Then press the send button.");

				} else if (action.equals("DEL")) {
					textName.setEditable(false);
					textNo.setEditable(true);
					textPosit.setEditable(false);
					text.setText("You have chosen the DEL method. \r\n"
							+ "Please enter a user number to delete information about it from the server. \r\n"
							+ "Example for input data: \r\n" + "\t \"empNo\":\"E01\" \r\n"
							+ "Then press the send button.");

				} else {
					textName.setEditable(false);
					textNo.setEditable(false);
					textPosit.setEditable(false);
					text.setText("You have not chosen a method. Please choose a method.");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		buttonSent.addSelectionListener(new SelectionListener() {

			int style = SWT.ICON_WORKING | SWT.OK;
			MessageBox messageBox = new MessageBox(shlWebclientcrud, style);

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int idx = combo.getSelectionIndex();
				String action = combo.getItem(idx);

				List<Object> srcLister = new ArrayList<Object>();
				LoadWeb load = new LoadWeb();
				load.loadString();
				load.saveFile();
				srcLister = load.parseJSON();
				StringBuilder builder = new StringBuilder(srcLister.size());

				if (action.equals("GET")) {

					for (int i = 0; i < srcLister.size(); i++) {
						builder.append(srcLister.get(i).toString());
						builder.append("\n");
					}
					text.setText(builder.toString());
					messageBox.setText("Ok");
					messageBox.setMessage("You have successfully read the data from the server.");
					messageBox.open();

				} else if (action.equals("POST")) {

					String tmpName = textName.getText().replaceAll("[^A-Za-z0-9]", "");
					String tmpNo = textNo.getText().replaceAll("[^A-Za-z0-9]", "");
					String tmpPosit = textPosit.getText().replaceAll("[^A-Za-z0-9]", "");
					System.out.println(tmpName);
					System.out.println(tmpNo);
					System.out.println(tmpPosit);

					if ((textName.getText().trim().length() == 0) | (textNo.getText().trim().length() == 0)
							| (textPosit.getText().trim().length() == 0)) {
						text.setText("You entered a string of spaces or consisting of several words.\r\n"
								+ "Please see the example when choosing a method.");

						style = SWT.ICON_ERROR | SWT.OK;
						messageBox.setText("Error");
						messageBox.setMessage("You made a mistake. Data didn't sent to the server.");
						messageBox.open();
					} else {
						try {
							load.sendPost(tmpName, tmpNo, tmpPosit);
							text.setText("You have successfully completed the POST method.");
							messageBox.setText("Ok");
							messageBox.setMessage("You have successfully wrote the data to the server.");
							messageBox.open();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} else if (action.equals("DEL")) {

					String tmpNo = textNo.getText().replaceAll("[^A-Za-z0-9]", "");
					System.out.println(tmpNo);
					if (textNo.getText().trim().length() == 0) {
						style = SWT.ICON_ERROR | SWT.OK;
						messageBox.setText("Error");
						messageBox.setMessage("You made a mistake. Data didn't delete to the server.");
						messageBox.open();
						text.setText("You entered a string of spaces or consisting of several words.\r\n"
								+ "Please see the example when choosing a method.");
					} else {
						try {
							load.delete(tmpNo);
							text.setText("You have successfully completed the DEL method.");

							messageBox.setText("Ok");
							messageBox.setMessage("You have successfully deleted the data from the server.");
							messageBox.open();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				} else {
					text.setText("You have not chosen a method. Please choose a method.");
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
	}

}
