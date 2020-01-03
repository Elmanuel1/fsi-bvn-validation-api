/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.splinx.nibss.util;

import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

/**
 *
 * @author Oluwatoba.Aribo
 */

public class LoggingUtil {

    public enum LogLevel {

        DEBUG, INFO, WARNING, ERROR, FATAL
    }

    public static String clean(String rawInput) {
        String cleaned;

        if (StringUtils.isEmpty(rawInput)) {
            cleaned = "";
        } else {
            cleaned = rawInput.replace('\n', '-').replace('\r', '_');
            cleaned = StringEscapeUtils.escapeHtml(cleaned);
            if (!rawInput.equals(cleaned)) {
                cleaned += "(Encoded)";
            }
        }

        return cleaned;
    }

    public static void log(final Logger logger, LoggingUtil.LogLevel logLevel, String msg, Throwable e) {
        if (logger == null || logLevel == null) {
            return;
        }

        //msg = PcidssUtils.blurCardNo(msg);
        switch (logLevel.ordinal()) {
            case 0:
               // logger.debug(clean(msg), e);
                break;
            case 1:
                logger.info(clean(msg), e);
                break;
            case 2:
                logger.warn(clean(msg), e);
                break;
            case 3:
                logger.error(clean(msg), e);
                break;
            case 4:
                logger.error(clean(msg), e);
                break;
            default:
                break;
        }


    }

    public static void log(final Logger logger, LoggingUtil.LogLevel logLevel, String msg) {
        if (logger == null || logLevel == null || msg == null) {
            return;
        }
        //msg = PcidssUtils.blurCardNo(msg);
        switch (logLevel.ordinal()) {
            case 0:
                logger.debug("msg: {}", clean(msg));
                break;
            case 1:
                logger.info("msg: {}", clean(msg));
                break;
            case 2:
                logger.warn("msg: {}", clean(msg));
                break;
            case 3:
                logger.error("msg: {}", clean(msg));
                break;
            case 4:
                logger.error("msg: {}", clean(msg));
                break;
            default:
                break;
        }
    }
}
