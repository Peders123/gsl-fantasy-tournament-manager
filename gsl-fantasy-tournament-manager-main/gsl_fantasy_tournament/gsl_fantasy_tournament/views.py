
from .serializers  import UserSerializer,TournamentSerializer,CaptainSerializer,PlayerSerializer
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from tournament.models import User,Tournament,Captain,Player

@api_view(['GET','POST','DELETE'])
def User_list (request):

    #get all Users, serialize and return them in json.
    if request.method == 'GET':
        user=User.objects.all()
        serializer = UserSerializer(user , many=True)
        return Response(serializer.data)
    #get the data sent 
    if request.method == 'POST':
       serializer = UserSerializer(data=request.data, many=True)
       if serializer.is_valid():
           serializer.save()
           return Response(serializer.data, status=status.HTTP_201_CREATED)
       else:    
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    if request.method == 'DELETE':
        user=User.objects.all()
        user.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
       
@api_view(['GET','PUT','DELETE'])
def User_detail(request,User_id):
    try:
        user=User.objects.get(pk=User_id)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer=UserSerializer(user)
        return Response(serializer.data)
    
    elif request.method == 'PUT':
        serializer = UserSerializer(user, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'DELETE':
        user.delete()
        return Response(status=status.HTTP_204_NO_CONTENT) 
    
@api_view(['GET','POST','DELETE'])
def Tournament_list (request):

    if request.method == 'GET':
          tournament=Tournament.objects.all()
          serializer = TournamentSerializer(tournament, many=True)
          return Response(serializer.data)
        
    if request.method == 'POST':
        serializer = TournamentSerializer(data=request.data, many=True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
           return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    if request.method == 'DELETE':
        tournament=Tournament.objects.all()
        tournament.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET','PUT','DELETE'])
def Tournament_detail (request,tournament_id):
    try:
        tournament=Tournament.objects.get(pk=tournament_id)
    except Tournament.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    if request.method == 'GET':
        serializer=TournamentSerializer(tournament)
        return Response (serializer.data)
    
    elif request.method == 'PUT':
        serializer = TournamentSerializer(tournament, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    elif request.method =='DELETE' :
        tournament.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET','POST','DELETE'])
def Captain_list (request):

    if request.method == 'GET':
        captain=Captain.objects.all()
        serializer = CaptainSerializer(captain , many=True)
        return Response(serializer.data)

    if request.method == 'POST':
       serializer = CaptainSerializer(data=request.data, many=True)
       if serializer.is_valid():
           serializer.save()
           return Response(serializer.data, status=status.HTTP_201_CREATED)
       else:    
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    if request.method == 'DELETE':
        captain=Captain.objects.all()
        captain.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    

@api_view(['GET','PUT','DELETE'])
def Captain_detail(request,captain_id):
    try:
        captain=Captain.objects.get(pk=captain_id)
    except Captain.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer=CaptainSerializer(captain)
        return Response(serializer.data)
    
    elif request.method == 'PUT':
        serializer = CaptainSerializer(captain, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET','POST','DELETE'])
def Player_list (request):

    if request.method == 'GET':
        player=Player.objects.all()
        serializer = PlayerSerializer(player , many=True)
        return Response(serializer.data)

    if request.method == 'POST':
       serializer = PlayerSerializer(data=request.data, many=True)
       if serializer.is_valid():
           serializer.save()
           return Response(serializer.data, status=status.HTTP_201_CREATED)
       else:    
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    if request.method == 'DELETE':
        player=Player.objects.all()
        player.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET','PUT','DELETE'])
def Player_detail(request,player_id):
    try:
        player=Player.objects.get(pk=player_id)
    except Player.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer=PlayerSerializer(player)
        return Response(serializer.data)
    
    elif request.method == 'PUT':
        serializer = PlayerSerializer(player, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

