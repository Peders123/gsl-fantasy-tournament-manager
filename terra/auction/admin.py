from django.contrib import admin

from .models import Room, Bidder, Biddee


class RoomAdmin(admin.ModelAdmin):

    list_display = ('room_id', 'current_asset', 'current_highest_bid', 'current_highest_bidder')


class BidderAdmin(admin.ModelAdmin):

    list_display = ('bidder_id', 'captain_id', 'join_order', 'currently_in')


class BiddeeAdmin(admin.ModelAdmin):

    list_display = ('biddee_id', 'bidder_id', 'player_id', 'bidded_amount', 'draft_order')


admin.site.register(Room, RoomAdmin)
admin.site.register(Bidder, BidderAdmin)
admin.site.register(Biddee, BiddeeAdmin)
