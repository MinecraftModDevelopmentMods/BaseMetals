// Use the target game's iron-bar parts and connection rules for each material.
const fs=require('node:fs'),path=require('node:path');
module.exports=function generateBars(namespace,states,models,textureFolder){
 let count=0;
 const directions=['north','east','south','west'];
 for(const file of fs.readdirSync(states).filter(n=>n.endsWith('_bars.json')).sort()){
  const name=file.slice(0,-5),texture=namespace+':'+textureFolder+'/'+name;
  const ref=part=>namespace+':block/'+name+'_'+part;
  const isolated=Object.fromEntries(directions.map(d=>[d,'false']));
  const parts=[{apply:{model:ref('post_ends')}},{when:isolated,apply:{model:ref('post')}}];
  directions.forEach((direction,index)=>{
   const cap={model:ref(index<2?'cap':'cap_alt')};
   const side={model:ref(index<2?'side':'side_alt')};
   if(index%2){cap.y=90;side.y=90;}
   parts.push({when:{...isolated,[direction]:'true'},apply:cap});
   parts.push({when:{[direction]:'true'},apply:side});
  });
  fs.writeFileSync(path.join(states,file),JSON.stringify({multipart:parts},null,2)+'\n');
  for(const part of ['post','post_ends','cap','cap_alt','side','side_alt']){
   fs.writeFileSync(path.join(models,name+'_'+part+'.json'),JSON.stringify({parent:'minecraft:block/iron_bars_'+part,textures:{particle:texture,bars:texture,edge:texture}},null,2)+'\n');
  }
  count++;
 }
 console.log('Generated vanilla-compatible bars for '+count+' '+namespace+' materials.');
};
if(require.main===module){
 const root=path.resolve(__dirname,'..');
 const namespace=fs.readFileSync(path.join(root,'gradle.properties'),'utf8').match(/^mod_id=([a-z]+)$/m)[1];
 const base=namespace==='basemetals';
 module.exports(namespace,path.join(root,base?'src/generated/resources':'src/main/resources','assets',namespace,'blockstates'),path.join(root,'src/main/resources/assets',namespace,'models/block'),base?'block':'blocks');
}
