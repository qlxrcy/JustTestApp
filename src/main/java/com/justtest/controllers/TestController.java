package com.justtest.controllers;

import antlr.StringUtils;
import com.justtest.utils.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by le.qi on 11/1/2016.
 */
@Controller
public class TestController {
    @RequestMapping(value="/index")
    public String index(){
        return"/index";
    }

    @RequestMapping(value="/test")
    public String test(){
        return"/test";
    }

    @RequestMapping(value="/test1")
    public String test1(){
        return"/test1";
    }

    @RequestMapping(value="/iat")
    public String iat(){
        return"/iat";
    }

    @ResponseBody
    @RequestMapping(value="/ajaxSave")
    public String ajaxSave(@RequestParam("message") String message){//, Map<String,Object> map, HttpServletRequest request){
//        String data = request.getParameter("message");
        String result="";
        try {
            if(token.isEmpty()){
                getToken();
            }
            String method1 = method1(message.replace("data:audio/wav;base64,", ""));
            JSONObject jsonObject = new JSONObject(method1);
            result = new JSONObject(method1).get("result").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value="/save")
    public String save( HttpServletRequest request,Model model){
        String data = request.getParameter("message");
        String result="";
        try {
            //decoderBase64File(data.replace("data:audio/wav;base64,", ""),"C:/Users/le.qi/Desktop/Record.wav");
            if(token.isEmpty()){
                getToken();
            }
            String method1 = method1(data.replace("data:audio/wav;base64,", ""));
            JSONObject jsonObject = new JSONObject(method1);
            //System.out.println(jsonObject.get("result"));
            result = new JSONObject(method1).get("result").toString();
            model.addAttribute("result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/test1";
    }
    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void decoderBase64File(String base64Code, String targetPath)
            throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.flush();
        out.close();

    }

    private static final String serverURL = "http://vop.baidu.com/server_api";
    private static String token = "";
    //private static final String testFileName = "C:/Users/le.qi/Desktop/test.pcm";
    private static final String testFileName = "C:/Users/le.qi/Desktop/Record.wav";
    //put your own params here
    private static final String apiKey = "MuoRBHWZ7ger0WoAGarFYQhb";
    private static final String secretKey = "72235d62c3ddf5335f183055844715b6";
    private static final String cuid = "8828740";

    private static void getToken() throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" +
                "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = new JSONObject(printResponse(conn)).getString("access_token");
    }

    private String method1(String speech) throws Exception {
        File pcmFile = new File(testFileName);
        byte[] buffer = new BASE64Decoder().decodeBuffer(speech);
        //speech = speech.replace("data:audio/wav;base64,", "");
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();

        // construct params
        JSONObject params = new JSONObject();
        params.put("format", "pcm");
        params.put("rate", 8000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", buffer.length);
        params.put("speech",speech );
        //params.put("len", pcmFile.length());
        //params.put("speech", DatatypeConverter.printBase64Binary(loadFile(pcmFile)));

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();

        String response = printResponse(conn);
        return response;
    }

    private static String printResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != 200) {
            // request error
            return "";
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println(new JSONObject(response.toString()).toString(4));
        return response.toString();
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
