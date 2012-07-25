package com.framework.mail;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-2-22
 * Time: 15:56:22
 * To change this template use File | Settings | File Templates.
 */
public class MailMessage {
    private String from;
 private String to;
 private String datafrom;
 private String datato;
 private String subject;
 private String content;
 private String date;
 private String user;
 private String password;

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public String getUser() {
  return user;
 }

 public void setUser(String user) {
  this.user = user;
 }

 public String getContent() {
  return content;
 }

 public void setContent(String content) {
  this.content = content;
 }

 public String getDatafrom() {
  return datafrom;
 }

 public void setDatafrom(String datafrom) {
  this.datafrom = datafrom;
 }

 public String getDatato() {
  return datato;
 }

 public void setDatato(String datato) {
  this.datato = datato;
 }

 public String getDate() {
  return date;
 }

 public void setDate(String date) {
  this.date = date;
 }

 public String getFrom() {
  return from;
 }

 public void setFrom(String from) {
  this.from = from;
 }

 public String getSubject() {
  return subject;
 }

 public void setSubject(String subject) {
  this.subject = subject;
 }

 public String getTo() {
  return to;
 }

 public void setTo(String to) {
  this.to = to;
 }
}
