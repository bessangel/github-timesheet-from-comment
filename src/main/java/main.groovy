package main.java

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Created by adalex on 9/25/15.
 */
/*
curl -sS -i -u 'email:PASS' "https://api.github.com/repos/PROJECTS/issues/comments?since=2015-09-20T00:00:00Z" | perl -e '$i=0;while(<>){$i=1 if /^\s*$/;  print $_ if $i;}' | tee /tmp/aaa

 */
def site = "https://api.github.com/repos/"
def repo = "winsite/ideapoly2_be"
//def repo = "winsite/iprace_be"
def api  = "issues/comments?since=2015-09-10T00:00:00Z"
def addr = String.format("%s%s/%s",site, repo, api)
def conn = addr.toURL().openConnection()
def user = "USER"
def password = "PASSWORD"
def auth = "${user}:${password}".getBytes().encodeBase64().toString()
conn.setRequestProperty("Authorization", "Basic ${auth}")
def j = new JsonSlurper();
def o= j.parse(new File("/tmp/aaa"))
//def o= j.parseText(conn.content.text)
def hours=0
def minut=0
def res = o.findAll{it.user.login == 'bessangel'}.groupBy{it.issue_url}.each {
//    print it.dump()
    println it.key
    it.value.each {
        (it.body =~ /^.*\:clock2\:\s+(?<h>\d+)h?\s*(?<m>\d+)m?/).each{g,h,m->
            println String.format("%1s\t%2s\t\tat %s", h,m, it.created_at)
            hours += (h?:0) as Integer
            minut += (m?:0) as Integer
        }
    }
}

println "\nItogo: ${hours}h ${minut}m"
