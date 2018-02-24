/*
 *    Calendula - An assistant for personal medication management.
 *    Copyright (C) 2016 CITIUS - USC
 *
 *    Calendula is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.time.cat.database;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.time.cat.TimeCatApp;
import com.time.cat.events.PersistenceEvents;
import com.time.cat.mvp.model.DBmodel.DBNote;
import com.time.cat.mvp.model.DBmodel.DBTask;
import com.time.cat.mvp.model.Note;
import com.time.cat.util.ModelUtil;

import java.sql.SQLException;
import java.util.List;


public class NoteDao extends GenericDao<DBNote, Long> {
    public static final String TAG = "NoteDao";

    public NoteDao(DatabaseHelper db) {
        super(db);
    }

    @Override
    public Dao<DBNote, Long> getConcreteDao() {
        try {
            return dbHelper.getNoteDao();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating users dao", e);
        }
    }

    @Override
    public void saveAndFireEvent(DBNote u) {

        Object event = u.getId() <= 0 ? new PersistenceEvents.NoteCreateEvent(u) : new PersistenceEvents.NoteUpdateEvent(u);
        save(u);
        TimeCatApp.eventBus().post(event);

    }

    public void createOrUpdateAndFireEvent(DBNote u) throws SQLException {

        Object event = u.getId() <= 0 ? new PersistenceEvents.NoteCreateEvent(u) : new PersistenceEvents.NoteUpdateEvent(u);
        createOrUpdate(u);
        TimeCatApp.eventBus().post(event);

    }

    public void updateAndFireEvent(DBNote u) {
        Object event = new PersistenceEvents.NoteUpdateEvent(u);
        try {
            update(u);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TimeCatApp.eventBus().post(event);
    }

    public void safeSaveDBNote(Note task) {
        Log.i(TAG, "返回的任务信息 --> " + task.toString());
        //保存用户信息到本地
        DBNote dbNote = ModelUtil.toDBNote(task);
        List<DBTask> existing = null;
        try {
            existing = DB.schedules().queryForEq("Url", dbNote.getUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (existing != null && existing.size() > 0) {
            long id = existing.get(0).getId();
            dbNote.setId(id);
            try {
                DB.notes().update(dbNote);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "更新笔记信息 --> updateAndFireEvent -- > " + dbNote.toString());
        } else {
            DB.notes().saveAndFireEvent(dbNote);
            Log.i(TAG, "保存笔记信息 --> saveAndFireEvent -- > " + dbNote.toString());
        }
    }

    public void updateActiveUserAndFireEvent(DBNote activeDBNote, Note user) {
//        Object event = new PersistenceEvents.UserUpdateEvent(activeDBNote);
//        try {
//            update(ModelUtil.toActiveDBNote(activeDBNote, user));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        TimeCatApp.eventBus().post(event);
    }

    /// Mange active user through preferences

    public void removeCascade(DBNote u) {
        // remove all data
//        removeAllStuff(u);
        // remove note
        DB.notes().remove(u);

    }

//    public void removeAllStuff(DBNote u) {
//        // remove notes
//        for (DBRoutine r : DB.routines().findAll()) {
//            if (r.user().id() == u.getId()) {
//                DB.routines().remove(r);
//            }
//        }
//    }
}
