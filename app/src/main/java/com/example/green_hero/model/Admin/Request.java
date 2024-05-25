package com.example.green_hero.model.Admin;

import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Request extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private ClassicUser user;
    private boolean status;
    private RecycleRequest recycle;

    public Request() {
    }

    public Request(ObjectId _id, ClassicUser user, boolean status, RecycleRequest recycle) {
        this._id = _id;
        this.user = user;
        this.status = status;
        this.recycle = recycle;
    }

    public Request(ClassicUser user, boolean status, RecycleRequest recycle) {
        this._id = new ObjectId();
        this.user = user;
        this.status = status;
        this.recycle = recycle;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public ClassicUser getUser() {
        return user;
    }

    public void setUser(ClassicUser user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public RecycleRequest getRecycle() {
        return recycle;
    }

    public void setRecycle(RecycleRequest recycle) {
        this.recycle = recycle;
    }
}
