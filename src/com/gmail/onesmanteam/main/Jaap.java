package com.gmail.onesmanteam.main;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("unused")
public class Jaap {

	private static Display display = new Display();
	private static Shell main = new Shell(display, SWT.SHELL_TRIM & (~SWT.RESIZE));

	private static int width = 1024;
	private static int resolutionW = 4;
	private static int resolutionH = 3;
	private static int height = width / resolutionW * resolutionH;

	private static int bookIndex = 0;
	private static int coverIndex = 0;

	private static Book curBook = null;
	private static Book[] books;

	private static Label descriptionLabel;
	private static Label coverImage;
	private static Label title;

	private static Listener nextBookListener = new Listener() {

		public void handleEvent(Event arg0) {
			if (bookIndex == books.length - 1) {
				bookIndex = 0;
			} else {
				bookIndex++;
			}

			setCurrentBook(bookIndex);
		}
	};
	private static Listener prevBookListener = new Listener() {

		public void handleEvent(Event arg0) {
			if (bookIndex == 0) {
				bookIndex = books.length - 1;
			} else {
				bookIndex--;
			}

			setCurrentBook(bookIndex);
		}
	};
	private static Listener nextCoverListener = new Listener() {

		public void handleEvent(Event e) {
			if (coverIndex == curBook.getCovers().length - 1) {
				coverIndex = 0;
			} else {
				coverIndex++;
			}

			setCover(coverIndex);
		}
	};
	private static Listener prevCoverListener = new Listener() {

		public void handleEvent(Event e) {
			if (coverIndex == 0) {
				coverIndex = curBook.getCovers().length - 1;
			} else {
				coverIndex--;
			}

			setCover(coverIndex);
		}
	};

	private static final int descriptive = 0;

	public static void main(String[] args) {
		main.setText("JAAP");
		main.setSize(width, height);
		main.setLayout(new FillLayout());
		main.setMaximized(true);
		main.open();

		loadBooks();
		setContent(main, descriptive);

		main.layout();

		while (!main.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}

		display.dispose();
	}

	public static void setContent(Shell s, int perspective) {
		if (perspective == descriptive) {
			Composite mainComp = new Composite(s, SWT.BORDER);

			RowLayout rl = new RowLayout();
			rl.marginLeft = 10;
			rl.marginHeight = 10;
			rl.marginRight = 10;
			rl.marginBottom = 10;
			rl.fill = true;
			rl.wrap = true;
			rl.spacing = 0;

			mainComp.setLayout(rl);
			mainComp.setBounds(0, 0, width, 500);

			Composite optionComp = new Composite(mainComp, SWT.BORDER);
			Composite titleComp = new Composite(mainComp, SWT.BORDER);
			Composite coverComp = new Composite(mainComp, SWT.BORDER);
			Composite descComp = new Composite(mainComp, SWT.BORDER);
			Composite buttonComp = new Composite(mainComp, SWT.BORDER);

			GridLayout buttonLayout = new GridLayout(5, false);
			GridData gridData = new GridData();
			gridData.grabExcessHorizontalSpace = false;
			gridData.horizontalAlignment = SWT.FILL;
			buttonLayout.

			buttonComp.setLayout(buttonLayout);

			Label optionPH = new Label(optionComp, SWT.NONE);
			optionPH.setText("OPTIONS HERE!");
			optionPH.setSize(main.getSize().x - 35, 30);

			title = new Label(titleComp, SWT.CENTER);

			FontData fD = title.getFont().getFontData()[0];
			fD.setHeight(16);
			title.setFont(new Font(display, new FontData(fD.getName(), fD.getHeight(), SWT.BOLD)));

			title.setText(curBook.getAuthor() + " - " + curBook.getName());
			title.setSize(main.getSize().x - 35, 45);

			coverImage = new Label(coverComp, SWT.BORDER);
			Image i = new Image(display, "archive/" + curBook.getCurrentCover());
			coverImage.setImage(i);
			coverImage.setSize(350, 500);

			descriptionLabel = new Label(descComp, SWT.WRAP);
			descriptionLabel.setSize(main.getSize().x - coverImage.getSize().x - 54, 500);
			descriptionLabel.setLocation(15, 20);
			descriptionLabel.setText(curBook.getDescription());

			Button nextCov = new Button(buttonComp, SWT.PUSH);
			nextCov.computeSize(200, SWT.DEFAULT);
			nextCov.setText("Next Cover");

			Button prevCov = new Button(buttonComp, SWT.PUSH);
			prevCov.setText("Previous Cover");

			Button nextBook = new Button(buttonComp, SWT.PUSH);
			nextBook.setText("Next Book");

			Button prevBook = new Button(buttonComp, SWT.PUSH);
			prevBook.setText("Previous Book");

			nextBook.addListener(SWT.Selection, nextBookListener);
			prevBook.addListener(SWT.Selection, prevBookListener);
			nextCov.addListener(SWT.Selection, nextCoverListener);
			prevCov.addListener(SWT.Selection, prevCoverListener);

			buttonComp.layout();
			mainComp.layout();
		}
	}

	public static void loadBooks() {
		File folder = new File("archive/data/books/");

		books = new Book[folder.list().length];

		for (int i = 0; i < folder.list().length; i++) {
			books[i] = new Book(folder.listFiles()[i].getAbsolutePath());
			System.out.println("Book added: " + books[i].getName());
		}

		if (curBook == null) {
			curBook = books[0];
		}
	}

	public static void setCurrentBook(int index) {
		if (curBook == books[index]) {
			return;
		}

		curBook = books[index];

		title.setText(curBook.getAuthor() + " - " + curBook.getName());
		descriptionLabel.setText(curBook.getDescription());
		coverImage.setImage(new Image(display, "archive/" + curBook.getCurrentCover()));
	}

	public static void setCover(int index) {
		if (curBook.getCurrentCover() == curBook.getCover(index)) {
			return;
		}

		curBook.setCurrentCover(index);
		coverImage.setImage(new Image(display, "archive/" + curBook.getCurrentCover()));
	}
}
