package com.example.androiddevassessment.utils;

import com.example.androiddevassessment.models.User;

import io.realm.Realm;

public class RealmUtils {
    public static void setUser(int id, String firstname, String lastname, String email, String avatar){
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user == null){
            user = new User();
        }

        realm.beginTransaction();
        user.setId(id);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setAvatar(avatar);
        user.setEmail(email);
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public static User getUser(){
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user == null){
            user = new User();
        }
        return user;
    }

    public static void setLogOutUser(boolean logged){
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user == null){
            user = new User();
        }

        realm.beginTransaction();
        user.setLoggedOut(logged);
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public static boolean checkStatus(){
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null){
            return user.isLoggedOut();
        }
        return false;
    }
}
