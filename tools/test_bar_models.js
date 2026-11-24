const {test}=require('node:test');
const assert=require('node:assert/strict');
const fs=require('node:fs');
const path=require('node:path');
const root=path.resolve(__dirname,'..');
const mod='basemetals';
function resources(){const result=new Map();for(const dir of ['src/main/resources','src/generated/resources']){
 const folder=path.join(root,dir,'assets',mod,'blockstates');if(!fs.existsSync(folder))continue;
 for(const name of fs.readdirSync(folder).filter(n=>n.endsWith('_bars.json')))result.set(name,JSON.parse(fs.readFileSync(path.join(folder,name),'utf8')));
}return result;}
test('every bars model follows vanilla connections without overlapping centre/caps',()=>{
 const states=resources();assert(states.size>=21);
 for(const [name,json] of states)for(let mask=0;mask<16;mask++){
  const directions=['north','east','south','west'];const state=Object.fromEntries(directions.map((d,i)=>[d,String(Boolean(mask&(1<<i)))]));
  const parts=json.multipart.filter(p=>!p.when||Object.entries(p.when).every(([k,v])=>String(v)===state[k])).map(p=>p.apply.model);
  const connections=directions.filter(d=>state[d]==='true').length;
  assert.equal(parts.filter(n=>n.endsWith('_post_ends')).length,1,name+' '+mask+' post ends');
  assert.equal(parts.filter(n=>n.endsWith('_post')).length,connections===0?1:0,name+' '+mask+' centre');
  assert.equal(parts.filter(n=>/_cap(_alt)?$/.test(n)).length,connections===1?1:0,name+' '+mask+' caps');
  assert.equal(parts.filter(n=>/_side(_alt)?$/.test(n)).length,connections,name+' '+mask+' sides');
 }
});

test('wider ore geometry keeps texture coordinates inside the intended sprite',()=>{
 const folder=path.join(root,'src/main/resources/assets/basemetals/models/block');let ores=0;
 for(const name of fs.readdirSync(folder).filter(n=>n.endsWith('_ore.json'))){
  const value=JSON.parse(fs.readFileSync(path.join(folder,name),'utf8'));if(!value.textures||!value.textures.overlay)continue;ores++;
  for(const element of value.elements)for(const face of Object.values(element.faces))assert.deepEqual(face.uv,[0,0,16,16],name);
 }
 assert.equal(ores,13);
});
