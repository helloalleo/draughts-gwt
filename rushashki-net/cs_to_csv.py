#! /usr/bin/env python
# -*- coding: utf-8 -*-

# Make sure App Engine APK is available
import os
import sys
import datetime
 
sys.path.append('/usr/local/google_appengine')
sys.path.append('/usr/local/google_appengine/lib/yaml/lib')

from google.appengine.api.files import records
from google.appengine.datastore import entity_pb
from google.appengine.api import datastore

dir_src = sys.argv[1]

vkProvider = fbProvider = gProvider = wndProvider = ndfProvider = 0

game_count = 0
data = []
addressesVk = []
addressesEmail = []
for root, dirs, files in os.walk(dir_src):
    for f in files:
        raw = open(os.path.join(root, f), 'r')
        reader = records.RecordsReader(raw)
        for record in reader:
            entity_proto = entity_pb.EntityProto(contents=record)
            entity = datastore.Entity.FromPb(entity_proto)
            email = playerName = ''
            vk = fb = g = w = False
            visitCounter = 0
            lastVisited = datetime.datetime(year=2000, month=1, day=1)
            for i in entity.items():
                k = i[0]
                v = i[1]
                if k == 'playDate':
                    game_count += 1
                if k == 'authProviderName':
                    if v == 'VKontakte':
                        vk = True
                        vkProvider += 1
                    elif v == 'Facebook':
                        fb = True
                        fbProvider += 1
                    elif v == 'Google':
                        g = True
                        gProvider += 1
                    elif v == 'Windows Live':
                        w = True
                        wndProvider += 1
                    else:
                        gProvider += 1
                        g = True
                else:
                    if k == 'email':
                        email = v
                    if k == 'playerName':
                        playerName = v
                    if k == 'visitCounter':
                        visitCounter = v
                    if k == 'lastVisited' and v is not None:
                        lastVisited = v
            if vk:
                addressesVk += (email + ' ' + str(visitCounter),)
            else:
                addressesEmail += (email + ' ' + str(visitCounter),)
            if vk:
                data += ((playerName, email, None, None, None, visitCounter, lastVisited),)
            elif fb:
                data += ((playerName, None, email, None, None, visitCounter, lastVisited),)
            elif g:
                data += ((playerName, None, None, g, None, visitCounter, lastVisited),)
            elif w:
                data += ((playerName, None, None, None, w, visitCounter, lastVisited),)
            else:
                assert False, 'Unrecognized provider'

visitField = 5
data_sorted = sorted(data, key=lambda x: x[visitField], reverse=True)

emails = open('emails.txt', 'w')
vkAddr = open('vk.txt', 'w')
emails.write('\n'.join(addressesEmail))
vkAddr.write('\n'.join(addressesVk))
print(len(addressesVk))
print(len(addressesEmail))
#data_sorted = sorted(data, key=lambda x: x[3], reverse=True)
fav = []
for d in data_sorted:
    if d[visitField] < 10:
        continue
    print d[0], d[1], d[2], d[3]
    fav += (d,)
print 'Favorites: {0}'.format(len(fav))
print 'VK: {0}'.format(vkProvider)
print 'Facebook: {0}'.format(fbProvider)
print 'Google: {0}'.format(gProvider)
print 'Windowsе: {0}'.format(wndProvider)
print 'Game count: {0}'.format(game_count)
