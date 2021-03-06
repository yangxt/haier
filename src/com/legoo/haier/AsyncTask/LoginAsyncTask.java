package com.legoo.haier.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.legoo.haier.Application.Haier;
import com.legoo.haier.AsyncTask.Base.NetworkAsyncTask;
import com.legoo.haier.AsyncTask.Callback.ModelEvent;
import com.legoo.haier.Handler.UserJsonHandler;
import com.legoo.haier.Handler.Json.JsonHandler;
import com.legoo.haier.Handler.Json.JsonOperation;
import com.legoo.haier.Model.UserModel;


/**
 * @class Authenticate AsyncTask
 * @author LeonNoW
 * @version 1.0
 * @date 2014-02-14
 */
public class LoginAsyncTask extends NetworkAsyncTask 
{
	private static final String NAME = "name";
	private static final String PASSWORD = "password";
	
	private String name;
	private String _password;
	
	public LoginAsyncTask(String account, String password)
	{
		super();
		name = account;
		_password = password;
	}
	
	@Override
	protected ModelEvent doInBackground(Void... params) 
	{   
		ModelEvent event = new ModelEvent(this);
		event.setMark(super.getMark());
		String url = Haier.getInstance().getDataService().postLogin();
		if (url != null)
		{
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(); 
		    pairs.add(new BasicNameValuePair(NAME, name));
		    pairs.add(new BasicNameValuePair(PASSWORD, _password));
		    
			UserJsonHandler handler;
			do
			{
				handler = (UserJsonHandler) JsonOperation.post(url, pairs, new UserJsonHandler());
			}
			while (retryTask(handler) == true);
			
			if (handler.getError() == JsonHandler.ERROR_NONE)
			{
				UserModel model = (UserModel) handler.getModel();
				model.setPassword(_password);
			}
			event.setError(handler.getError());
			event.setMessage(handler.getMessage());
			event.setModel(handler.getModel());
			pairs.clear();
			pairs = null;
			url = null;
			handler = null;
		}
		else 
		{
			event.setError(JsonHandler.ERROR_CONNECTION);
		}
		return event;
	}

//	UserModel model = new UserModel();
//	model.setName("name");
//	model.setPassword("password");
//	model.setDeviceid("sssssssss");
//	model.setId("id");
//	
//	event.setError(JsonHandler.ERROR_NONE);
//	event.setMessage("");
//	event.setModel(model);
}
