package com.as.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 远程执行linux的shell script
 *
 * @author Ickes
 * @since V0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Component
public class RemoteExecuteCommandutil {
    //字符编码默认是utf-8
    private static String DEFAULTCHART = "UTF-8";
    private Connection conn;

    @Value(value = "${linux.ip}")
    public String ip;

    @Value(value = "${linux.port}")
    public int port;

    @Value(value = "${linux.userName}")
    public String userName;

    @Value(value = "${linux.userPwd}")
    public String userPwd;


    /**
     * 远程登录linux的主机
     *
     * @return 登录成功返回true，否则返回false
     * @author Ickes
     * @since V0.1
     */
    public Boolean login() {
        if (conn != null && conn.isAuthenticationComplete()) {
            return true;
        }
        boolean flg = false;
        try {
            conn = new Connection(ip,port);
            conn.connect();//连接
            if(!conn.isAuthenticationComplete()) {
                flg = conn.authenticateWithPassword(userName, userPwd);//认证
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flg;
    }

    /**
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     * @author Ickes
     * 远程执行shll脚本或者命令
     * @since V0.1
     */
    public String execute(String cmd) {
        String result = "";
        try {
            if (login()) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                }
                //conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param cmd 即将执行的命令
     * @return 命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
     * @author Ickes
     * 远程执行shll脚本或者命令
     * @since V0.1
     */
    public String executeSuccess(String cmd) {
        String result = "";
        try {
            if (login()) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     * @author Ickes
     * @since V0.1
     */
    private String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
