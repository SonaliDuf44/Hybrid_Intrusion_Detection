/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package final_project;

/**
 *
 * @author sahil
 */
public class PacketWrap {
    private final double duration;
    private final int protocol;
    private final int service;
    private final int flag;
    private final double src_bytes;
    private final double dst_bytes;
    private final int land;
    private final double wrong_fragment;
    private final double urgent;
    private final double hot;
    private final double num_failed_logins;
    private final int logged_in;
    private final double num_compromised;
    private final double root_shell;
    private final double su_attempted;
    private final double num_root;
    private final double num_file_creations;
    private final double num_shells;
    private final double num_access_files;
    private final double num_outbound_cmds;
    private final int is_host_login;
    private final int is_guest_login;
    private final double count;
    private final double srv_count;
    private final double serror_rate;
    private final double srv_serror_rate;
    private final double rerror_rate;
    private final double srv_rerror_rate;
    private final double same_srv_rate;
    private final double srv_diff_host_rate;
    private final double diff_srv_rate;
    private final double dst_host_count;
    private final double dst_host_srv_count;
    private final double dst_host_same_srv_rate;
    private final double dst_host_diff_srv_rate;
    private final double dst_host_same_src_port_rate;
    private final double dst_host_srv_diff_host_rate;
    private final double dst_host_serror_rate;
    private final double dst_host_srv_serror_rate;
    private final double dst_host_rerror_rate;
    private final double dst_host_srv_rerror_rate;
   
    public PacketWrap(double duration, int protocol, int service, int flag, double src_bytes,
            double dst_bytes,int land, double wrong_fragment,double urgent, double hot, double num_failed_logins,
            int logged_in, double num_compromised,double root_shell,double su_attempted, double num_root,
            double num_file_creations, double num_shells, double num_access_files, double num_outbound_cmds,
            int is_host_login, int is_guest_login, double count, double srv_count, double serror_rate,
            double srv_serror_rate, double rerror_rate, double srv_rerror_rate, double same_srv_rate,
            double diff_srv_rate, double srv_diff_host_rate, double dst_host_count, double dst_host_srv_count,
            double dst_host_same_srv_rate, double dst_host_diff_srv_rate, double dst_host_same_src_port_rate,
            double dst_host_srv_diff_host_rate, double dst_host_serror_rate, double dst_host_srv_serror_rate,
            double dst_host_rerror_rate, double dst_host_srv_rerror_rate) {
        this.duration = duration;
        this.protocol = protocol;
        this.service = service;
        this.flag= flag;
        this.src_bytes = src_bytes;
        this.dst_bytes = dst_bytes;
        this.land = land;
        this.wrong_fragment = wrong_fragment;
        this.urgent = urgent; 
        this.hot = hot;
        this.num_failed_logins = num_failed_logins;
        this.logged_in = logged_in;
        this.num_compromised = num_compromised;
        this.root_shell = root_shell;
        this.su_attempted = su_attempted;
        this.num_root = num_root;
        this.num_file_creations = num_file_creations;
        this.num_shells = num_shells; 
        this.num_access_files = num_access_files;
        this.num_outbound_cmds = num_outbound_cmds;
        this.is_host_login = is_host_login;
        this.is_guest_login = is_guest_login;
        this.count = count;
        this.srv_count = srv_count; 
        this.serror_rate = serror_rate;
        this.srv_serror_rate = srv_serror_rate;
        this.rerror_rate = rerror_rate;
        this.srv_rerror_rate = srv_rerror_rate;
        this.same_srv_rate = same_srv_rate;
        this.diff_srv_rate = diff_srv_rate;
        this.srv_diff_host_rate = srv_diff_host_rate;
        this.dst_host_count = dst_host_count;
        this.dst_host_srv_count = dst_host_srv_count;
        this.dst_host_same_srv_rate =dst_host_same_srv_rate;
        this.dst_host_diff_srv_rate = dst_host_diff_srv_rate;
        this.dst_host_same_src_port_rate =dst_host_same_src_port_rate;
        this.dst_host_srv_diff_host_rate = dst_host_srv_diff_host_rate; 
        this.dst_host_serror_rate = dst_host_serror_rate;
        this.dst_host_srv_serror_rate = dst_host_srv_serror_rate;
        this.dst_host_rerror_rate = dst_host_srv_rerror_rate;
        this.dst_host_srv_rerror_rate = dst_host_srv_rerror_rate; 
    }
}
