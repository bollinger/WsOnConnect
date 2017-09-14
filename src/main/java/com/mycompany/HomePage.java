package com.mycompany;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {

	public HomePage(final PageParameters parameters) {
		super(parameters);
	}

  @Override
  protected void onInitialize() {
    super.onInitialize();
    add(new MyPanel("myPanel"));
  }
}
