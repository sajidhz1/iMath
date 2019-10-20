const functions = require('firebase-functions');
const {WebhookClient} = require('dialogflow-fulfillment');
const {QuestionService} = require('./question-service');

exports.dialogflowFirebaseFulfillment = functions.https.onRequest((request, response) => {
    const agent = new WebhookClient({ request, response });
    const qs = new QuestionService();
    console.log('Dialogflow Request headers: ' + JSON.stringify(request.headers));
    console.log('Dialogflow Request body: ' + JSON.stringify(request.body));
   
    function welcome(agent) {
      agent.add(`Welcome to my agent!`);
    }

    function greeting(agent){
        agent.add('Hello There!');
    }

    function questionStart(agent){
        agent.add(qs.getNextQuestion("q1"));
    }

    function getNextQuestion(agent){
        let question = qs.getNextQuestion(request.body.queryResult.parameters.questionId);
        agent.add(question);
    }

    function askForHelp(agent){
        let help = qs.getHelpForQuestion(request.body.queryResult.parameters.questionId);
        agent.add(help);
    }

    function getSolveMethod(agent){
        let method = qs.getSolveMethodForQuestion(request.body.queryResult.parameters.questionId);
        agent.add(method);
    }

    function answer(agent) {
        let answer = request.body.queryResult.parameters.answer;
        let q = request.body.queryResult.parameters.questionId;
        if(qs.getAnswerForQuestion(q) == answer){
            if(q == qs.getLast().questionId){
                agent.add("Amazing, you are correct! The questionarie has been completed. Lets see how much you have taken.. ");
            }else{
            var possibleResponse = [
                "Amazing, you are correct. Let's move on to the next question..",
                "Marvelous! I can see you improving, You are correct!",
                "Wow! that was actually a hard one. Now the next question.."
              ];
              
              var pick = Math.floor( Math.random() * possibleResponse.length );
              
              var response = possibleResponse[pick];
              agent.add(response);
            }
        }else{
            if(q == qs.getLast().questionId){
                agent.add("That was so close! but not correct, The questionarie has been completed. Lets see how much you have taken.. ");
            }else{
            var possibleResponse = [
                "That was so close but not correct. The answer is "+qs.getAnswerForQuestion(q),
                "Unfortunately that's not correct. The answer is "+qs.getAnswerForQuestion(q)
              ];
              
              var pick = Math.floor( Math.random() * possibleResponse.length );

              var response = possibleResponse[pick];
              agent.add(response);
            }
        }
    }
   
    function fallback(agent) {
      agent.add(`I didn't understand`);
      agent.add(`I'm sorry, can you try again?`);
    }

    let intentMap = new Map();
    intentMap.set('Default Welcome Intent', welcome);
    intentMap.set('Default Fallback Intent', fallback);
    intentMap.set('Greeting',greeting);
    intentMap.set('start-question',questionStart);
    intentMap.set('help',askForHelp);
    intentMap.set('answer',answer);
    intentMap.set('next-question',getNextQuestion);
    intentMap.set('solve',getSolveMethod);
    // intentMap.set('your intent name here', yourFunctionHandler);
    // intentMap.set('your intent name here', googleAssistantHandler);
    agent.handleRequest(intentMap);
  });
  
