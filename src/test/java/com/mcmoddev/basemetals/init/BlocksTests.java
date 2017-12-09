package com.mcmoddev.basemetals.init;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;

class BlocksTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
//		Blocks.registerVanilla() ; 
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Test
//	void testRegisterVanillaRegistersCoalBlock() {
//		final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
//		
//		Block block  = coal.getBlock(net.minecraft.init.Blocks.COAL_BLOCK.getRegistryName().getResourcePath());
//		
//		assertNotNull(block);
//	}

}
