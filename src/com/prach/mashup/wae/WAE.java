package com.prach.mashup.wae;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WAE extends Activity {
	public int pagecount = 0;
	private  WebView browser = null;
	private final int BARCODE_INTENT = 0x002;
	private JSHandler jsh;
	public boolean took = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		browser = (WebView) findViewById(R.id.browser);
		
		WebSettings settings = browser.getSettings();
		settings.setJavaScriptEnabled(true);
		browser.clearCache(true);

		browser.setWebChromeClient(new WebChromeClient());
		//loadPagesA("9780470344712");
		//loadPagesB("Professional Android Application Development");
		if(!took)
			takeBarcode();
	}
	
	public void loadPagesA(final String barcode){
		jsh = new JSHandler();
		browser.addJavascriptInterface(jsh,"prach");
		
		pagecount=0;
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view,String url,Bitmap favicon){
				Log.d("com.prach.mashup.WAE","Loading["+pagecount+"]:\n"+url);
				Toast.makeText(getApplicationContext(), "Loading["+pagecount+"]:\n"+url, Toast.LENGTH_SHORT).show();
			}

			@Override  
			public void onPageFinished(WebView view, String url){  
				if(pagecount==0){
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:page load finished");
					Toast.makeText(getApplicationContext(), "Page["+pagecount+"]:finished", Toast.LENGTH_SHORT).show();
					browser.loadUrl("javascript:"+
							"var tagArray1 = document.getElementsByTagName('form');\n"+
							"var parentElement;\n"+
							"for(var i=0;i<tagArray1.length;i++)\n"+
							"    if(i==0&&tagArray1[i].name=='site-search')\n"+
							"        parentElement = tagArray1[i];\n"+
							"var tagArray2 = parentElement.getElementsByTagName('input');\n"+
							"var childElement;\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(i==0&&tagArray2[i].id=='twotabsearchtextbox'&&tagArray2[i].name=='field-keywords'&&tagArray2[i].className=='searchSelect')\n"+
							"        childElement = tagArray2[i];\n"+
							"childElement.focus();\n"+
							"childElement.value='"+barcode+"';\n"+
							"childElement.form.submit();\n"
					);  
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:script load finished");
					pagecount++;
				}else if(pagecount==1){
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:page load finished");
					Toast.makeText(getApplicationContext(), "Page["+pagecount+"]:finished", Toast.LENGTH_SHORT).show();
					browser.loadUrl("javascript:"+
							"window.prach.setsavestate('false');\n"+
							"var tagArray1 = document.getElementsByTagName('div');\n"+
							"var parentElement;\n"+
							"for(var i=0;i<tagArray1.length;i++)\n"+
							"    if(i>44&&i<56&&tagArray1[i].className=='productTitle')\n"+
							"        parentElement = tagArray1[i];\n"+
							"var tagArray2 = parentElement.getElementsByTagName('a');\n"+
							"var childElement;\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(i==0)"+
							"        childElement = tagArray2[i];\n"+
							"childElement.focus();\n"+
							"var ProductTitle = childElement.textContent;\n"+
							"window.prach.save('ProductTitle',ProductTitle);\n"+
							"window.prach.setsavestate('true');\n"
					);
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:script load finished");
					 
					while(!jsh.saved){}
					Log.d("com.prach.mashup.WAE", "ProductTitle="+jsh.ProductTitle);
					loadPagesB(jsh.ProductTitle);
				}
			}  
		}); 
		browser.loadUrl("http://www.amazon.com/?ie=UTF8&force-full-site=1");
	}
	
	public void loadPagesB(final String productTitle){
		jsh = new JSHandler();
		browser.addJavascriptInterface(jsh,"prach");
		
		Log.d("com.prach.mashup.WAE", "loadPagesB:"+productTitle);
		pagecount=0;
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view,String url,Bitmap favicon){
				Log.d("com.prach.mashup.WAE","Loading["+pagecount+"]:\n"+url);
				//Toast.makeText(getApplicationContext(), "Loading["+pagecount+"]:\n"+url, Toast.LENGTH_SHORT).show();
			}
 
			@Override  
			public void onPageFinished(WebView view, String url){  
				if(pagecount==0){
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:page load finished");
					Toast.makeText(getApplicationContext(), "Page["+pagecount+"]:finished", Toast.LENGTH_SHORT).show();
					browser.loadUrl("javascript:"+
							"var tagArray1 = document.getElementsByTagName('form');\n"+
							"var parentElement;\n"+
							"for(var i=0;i<tagArray1.length;i++)\n"+
							"    if(i==0&&tagArray1[i].name=='headerSearchForm')\n"+
							"        parentElement = tagArray1[i];"+
							"var tagArray2 = parentElement.getElementsByTagName('input');\n"+
							"var childElement;\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(i==1&&tagArray2[i].id=='search_query'&&tagArray2[i].name=='search[query]')\n"+
							"        childElement = tagArray2[i];"+
							"childElement.focus();\n"+
							"childElement.value = '"+productTitle+"';\n"+
							"childElement.form.submit();\n"
					);
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:script load finished");
					pagecount++;
				}else if(pagecount==1){
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:page load finished");
					Toast.makeText(getApplicationContext(), "Page["+pagecount+"]:finished", Toast.LENGTH_SHORT).show();
					browser.loadUrl("javascript:"+
							"var tagArray1 = document.getElementsByTagName('table');\n"+
							"var parentElement;\n"+
							"for(var i=0;i<tagArray1.length;i++)\n"+
							"    if(i==0&&tagArray1[i].className=='tableList')\n"+
							"        parentElement = tagArray1[i];\n"+
							"var tagArray2 = parentElement.getElementsByTagName('a');\n"+
							"var childElement;\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(i==1&&tagArray2[i].className=='bookTitle')\n"+
							"        childElement = tagArray2[i];\n"+
							"childElement.focus();\n"+
							"window.location = childElement.href;\n"
					);
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:script load finished");
					pagecount++;
				}else if(pagecount==2){
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:page load finished");
					Toast.makeText(getApplicationContext(), "Page["+pagecount+"]:finished", Toast.LENGTH_SHORT).show();
					browser.loadUrl("javascript:"+
							"window.prach.setsavestate('false');\n"+
							"var tagArray1 = document.getElementsByTagName('div');\n"+
							"var parentElement;\n"+
							"for(var i=0;i<tagArray1.length;i++)\n"+
							"   if(i>9&&i<17&&tagArray1[i].className=='leftAlignedImage')\n"+
							"       parentElement = tagArray1[i];\n"+
							"var tagArray2 = parentElement.getElementsByTagName('img');\n"+
							"var childElement;\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(i==0)\n"+
							"        childElement = tagArray2[i];\n"+
							"var BookImage = childElement.src;\n"+
							"window.prach.save('BookImage',BookImage);\n"+
							
							"var tagArray1 = document.getElementsByTagName('div');\n"+
							"var parentElement;\n"+ 
							"for(var i=0;i<tagArray1.length;i++){\n"+
							"    if(i>32&&i<40)window.prach.info(i+':'+tagArray1[i].className);"+
							//"    if(i==37&&(tagArray1[i].className.indexOf('infoBoxRowItem')!=-1||tagArray1[i].className.indexOf('reviewText')!=-1)){\n"+
							"    if((i>(32))&&(i<42)&&(tagArray1[i].className.indexOf('infoBoxRowItem')!=-1&&tagArray1[i].className.indexOf('reviewText')!=-1)){\n"+
							"        parentElement = tagArray1[i];break;}\n" +
							"}"+
							"var tagArray2 = parentElement.getElementsByTagName('div');\n"+
							"var childElement;\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(i==0&&tagArray2[i].className=='floatingBox')\n"+
							"        childElement = tagArray2[i];\n"+
							"var BookDescription = childElement.innerHTML;\n"+
							"window.prach.save('BookDescription',BookDescription);\n"+		
							/*"var tagArray1 = document.getElementsByTagName('div');\n"+
							"var parentElement;\n"+
							"for(var i=0;i<tagArray1.length;i++){\n"+
							"    if(i<72&&i>64)window.prach.info(i+':'+tagArray1[i].className);"+
							//"    if(i==68&&tagArray1[i].className=='bigBoxContent')\n"+
							"    if(i>65&&i<71&&tagArray1[i].className=='bigBoxContent'){\n"+
							"        parentElement = tagArray1[i];break;}\n"+ 
							"}"+*/
							"var tagArray2 = document.getElementsByTagName('span');\n"+
							"var childElement;\n"+
							"var BookReview = '';\n"+
							"for(var i=0;i<tagArray2.length;i++)\n"+
							"    if(tagArray2[i].className=='reviewText'){\n"+
							"        childElement = tagArray2[i];\n"+
							"        BookReview += (childElement.textContent+'\\n');\n"+
							"    }\n"+
							"window.prach.save('BookReview',BookReview);\n"+ 
							"window.prach.setsavestate('true');\n"
					);
					Log.d("com.prach.mashup.WAE","Page["+pagecount+"]:script load finished");
					
					while(!jsh.saved){}
					loadPagesC(); 
				}
			}  
		}); 
		browser.loadUrl("http://www.goodreads.com/");
		//browser.loadUrl("http://www.goodreads.com/book/show/3429846.Professional_Android_Application_Development");
	}
	
	public void loadPagesC(){
		Log.d("com.prach.mashup.WAE", "loadPagesC();");
		pagecount=0;
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view,String url,Bitmap favicon){
				Log.d("com.prach.mashup.WAE","Loading["+pagecount+"]:\n"+url);
				//Toast.makeText(getApplicationContext(), "Loading["+pagecount+"]:\n"+url, Toast.LENGTH_SHORT).show();
			}
		});
		//browser.loadUrl("http://www.amazon.com/?ie=UTF8&force-full-site=1");
		StringBuffer htmlcode = new StringBuffer();
		htmlcode.append("<html><body><div align='center'><big>GoodReads Result</big><br>");
		htmlcode.append("<img src='");
		htmlcode.append(jsh.BookImage); 
		htmlcode.append("'/><br><big>Descriptions:</big><br></div>");
		htmlcode.append(jsh.BookDescription);
		htmlcode.append("<div align='center'><br><big>Reviews:</big><br></div>");
		htmlcode.append(jsh.BookReview.replaceAll("\\\n", "<br>"));
		htmlcode.append("</body></html>");
		browser.loadData(htmlcode.toString(), "text/html", "utf-8");
	}

	public class JSHandler{
		public String ProductTitle;
		public String BookDescription = "";
		public String BookReview = "";
		public String BookImage = ""; 
		public boolean saved = false;
		
		public JSHandler(){
			Log.d("com.prach.mashup.WAE", "JSHandler();");
		} 
		
		public void save(String param,String input){
			//saved = false;
			Log.d("com.prach.mashup.WAE", "save();"); 
			if(param.equals("ProductTitle")){
				Log.d("com.prach.mashup.WAE", "input:"+input);
				ProductTitle = input.split(" \\(")[0]; 
				Log.d("com.prach.mashup.WAE", "ProductTitle:"+ProductTitle);
			}else if(param.equals("BookDescription")){
				BookDescription = removeTags(input);
				Log.d("com.prach.mashup.WAE", "BookDescription:"+BookDescription);
			}else if(param.equals("BookReview")){
				BookReview = input;
				Log.d("com.prach.mashup.WAE", "BookReview:"+BookReview);
			}else if(param.equals("BookImage")){
				BookImage = input;
				Log.d("com.prach.mashup.WAE", "BookImage:"+BookImage);
			}
			//saved=true;
		}
		
		public void info(String input){
			Log.i("com.prach.mashup.WAE", "info:"+input);
		}
		
		public void setsavestate(String state){
			if(state.equals("true"))
				saved = true;
			else if(state.equals("false"))
				saved = false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if (keyCode == KeyEvent.KEYCODE_BACK) { 
			browser.stopLoading();
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        startActivityForResult(intent, BARCODE_INTENT);
	        return true;
		}else if (keyCode == KeyEvent.KEYCODE_HOME) { 
			Toast.makeText(getApplicationContext(), "Quit Application", Toast.LENGTH_SHORT).show();
			finish();
			return true;
		}else return false;
		//return super.onKeyDown(keyCode, event);
	}

	public void loadUrl(String url){
		Toast.makeText(this, "Loading["+pagecount+"]:\n"+browser.getUrl(), Toast.LENGTH_SHORT).show();
		browser.loadUrl(url);
	}

	@Override
	protected void onResume(){
		/*if(!took){
			takeBarcode();
			pagecount=0;
		}*/
		super.onResume();
	}


	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == BARCODE_INTENT) {
			if (resultCode == RESULT_OK) {
				took = true;
				String contents = intent.getStringExtra("SCAN_RESULT");
				//String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.d("com.prach.mashup.WAE", "BarcodeContents:"+contents);
				pagecount = 0;
				loadPagesA(contents);
				//showDialog("Scan Succeed", "Format: " + format + "\nContents: " + contents);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Scan Canceled", Toast.LENGTH_SHORT).show();
				took = true;
				//setContentView(browser);
				//finish();
			}
		} 
	}
	
	public void takeBarcode(){
		took=true;
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        startActivityForResult(intent, BARCODE_INTENT);	
	}
	
	public String removeTags(String str){
		String originalString = str.replaceAll("\\<.*?\\>", "<>");
		originalString = originalString.replaceAll("<><>", ",\n");
		originalString = originalString.replaceAll("<>", "");
		return originalString;
	}
}