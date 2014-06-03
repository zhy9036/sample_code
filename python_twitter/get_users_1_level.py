"""
Yang Zhang

get_twitter_users(user_id) function prints the follower 
count of passed in user name. And follower counts of all follower
this user (1 DEPTH)

"""

import tweepy
import time

def get_twitter_users(user_screen_name):

	consumer_key = 'h6xWpkPMQgR2p9SHGJj4pqea6'
	consumer_secret = '08A6m63UFLgiSJNYiNUrDTSG6peALFnN4ESjcESwATFZ7uWTHa'
	access_token = '442148580-PbPAFaZfpqZzKywVm1F64UKB30FD3EfDyW4ELAIw'
	access_token_secret = 'xNHmbN3gC28MxUyQ98LGZt4RZ8jgd1uSKSsipsWOYNRTj' 

	auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
	auth.set_access_token(access_token, access_token_secret)

	api = tweepy.API(auth)

	#print api.rate_limit_status()
	user = api.get_user(user_screen_name)
	print "\n\n*************************************"
	print "Current User: ",
	print user.screen_name
	print "has",
	print user.followers_count,
	print "follower(s)"

	print "\nAnd First Level of the Graph:"
	count = 0
	ids = api.followers_ids(user_screen_name)
	print "count",
	print "%-16s %s\n"%("Screen Name", "Followers Count")
	for id in ids:
		time.sleep(10) #slower the api access rate, in case of rate limit error
		count += 1
		print count, 
		print "    %-25s %s\n"%(api.get_user(id).screen_name, api.get_user(id).followers_count), 
	print "\n*************************************\n\n"

	"""
	for hello in tweepy.Cursor(api.followers, user_id = user_screen_name).items():
		time.sleep(10)
		print hello.screen_name

	
	ha = tweepy.Cursor(api.followers, screen_name = user_screen_name)
	for follower_cursor in ha.items():	
		time.sleep(10)
		print follower_cursor.screen_name
	"""

#test the function
name = raw_input("Input a id/screen_name: ")
get_twitter_users(name)